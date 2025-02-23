package com.example.sideproject.domain.pr.repository.query;

import com.example.sideproject.domain.pr.dto.PublicResumesListResponseDto;
import com.example.sideproject.domain.pr.dto.PublicResumesResponseDto;
import com.example.sideproject.domain.pr.entity.PublicResumes;
import com.example.sideproject.domain.pr.entity.QPublicResumes;
import com.example.sideproject.domain.resume.entity.QResumeTechStack;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.techstack.dto.TechStackMappingDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PublicResumesQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private QPublicResumes qPublicResumes = QPublicResumes.publicResumes;

    public Page<PublicResumesListResponseDto> getPublicResumes(Pageable pageable, Sort sort) {
        List<PublicResumesListResponseDto> publicResumes = jpaQueryFactory.select(Projections.constructor(
                        PublicResumesListResponseDto.class,
                        qPublicResumes.id,
                        qPublicResumes.resume.id,
                        qPublicResumes.resume.title,
                        qPublicResumes.resume.workType,
                        qPublicResumes.viewCount,
                        qPublicResumes.commentCount,
                        qPublicResumes.favoriteCount
                )).from(qPublicResumes)
                .leftJoin(qPublicResumes.resume)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(getOrderSpecifier(sort))
                .fetch();

        List<Long> resumeIds = publicResumes.stream()
                .map(PublicResumesListResponseDto::getResumeId)
                .toList();

        List<TechStackMappingDto> techStacks = getTechStacks(resumeIds);

        Map<Long, List<String>> techStackMap = techStacks.stream().collect(Collectors.groupingBy(TechStackMappingDto::id,
                Collectors.mapping(TechStackMappingDto::name, Collectors.toList())));

        List<PublicResumesListResponseDto> result = publicResumes.stream()
                .map(pr -> pr.addTechStacks(techStackMap.get(pr.getResumeId()))).toList();

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery().fetchOne());
    }

    private List<TechStackMappingDto> getTechStacks(List<Long> resumeIds) {
        QResumeTechStack qResumeTechStack = QResumeTechStack.resumeTechStack;

        List<TechStackMappingDto> techStacks = jpaQueryFactory.select(Projections.constructor(
                        TechStackMappingDto.class,
                        qResumeTechStack.resume.id,
                        qResumeTechStack.techStack.id,
                        qResumeTechStack.techStack.name
                ))
                .from(qResumeTechStack)
                .join(qResumeTechStack.techStack)
                .where(
                        qResumeTechStack.resume.id.in(resumeIds)
                )
                .fetch();
        return techStacks;
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<?> expression = new PathBuilder<>(Resume.class, "resume");
            orders.add(new OrderSpecifier<>(direction, expression.get(order.getProperty(), Comparable.class)));
        });
        return orders.toArray(OrderSpecifier[]::new);
    }


    private JPAQuery<Long> countQuery() {
        return jpaQueryFactory.select(qPublicResumes.count())
                .from(qPublicResumes);
    }
}
