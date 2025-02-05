package com.example.sideproject.domain.resume.repository.query;

import com.example.sideproject.domain.pr.dto.PrResponseDto;
import com.example.sideproject.domain.resume.dto.TechStackDto;
import com.example.sideproject.domain.resume.entity.QResume;
import com.example.sideproject.domain.resume.entity.QResumeTechStack;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.resume.entity.ResumeTechStack;
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
public class ResumeQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private QResume qResume = QResume.resume;

    public Page<PrResponseDto> getPublishedResumes(Pageable pageable) {
        List<PrResponseDto> resumes = jpaQueryFactory.select(Projections.constructor(
                        PrResponseDto.class,
                        qResume.id,
                        qResume.user.nickname,
                        qResume.position,
                        qResume.title,
                        qResume.introduce,
                        qResume.workType,
                        qResume.publishedAt
                )).from(qResume)
                .where(
                        qResume.publishedAt.isNotNull()
                )
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        List<Long> resumeIds = resumes.stream().map(PrResponseDto::getPrId).toList();

        QResumeTechStack qResumeTechStack = QResumeTechStack.resumeTechStack;

        List<TechStackDto> techStacks = jpaQueryFactory.select(Projections.constructor(
                        TechStackDto.class,
                        qResumeTechStack.techStack.id,
                        qResumeTechStack.techStack.name
                ))
                .from(qResumeTechStack)
                .join(qResumeTechStack.techStack)
                .where(
                        qResumeTechStack.resume.id.in(resumeIds)
                )
                .fetch();

        Map<Long, List<String>> techStackMap = techStacks.stream().collect(Collectors.groupingBy(TechStackDto::techStackId,
                Collectors.mapping(TechStackDto::name, Collectors.toList())));

        List<PrResponseDto> result = resumes.stream().map(resume -> resume.setTechStacks(techStackMap.get(resume.getPrId()))).toList();

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery().fetchOne());
    }

    private JPAQuery<Long> countQuery() {
        return jpaQueryFactory.select(qResume.count())
                .from(qResume)
                .where(
                        qResume.publishedAt.isNotNull()
                );
    }

    private OrderSpecifier[] getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC : Order.DESC;
            PathBuilder<Resume> expression = new PathBuilder<>(Resume.class, "resume");
            orders.add(new OrderSpecifier(direction, expression.get(order.getProperty())));
        });
        return orders.toArray(OrderSpecifier[]::new);
    }
}
