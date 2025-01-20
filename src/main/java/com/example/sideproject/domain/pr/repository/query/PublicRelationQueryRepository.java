package com.example.sideproject.domain.pr.repository.query;

import com.example.sideproject.domain.pr.dto.read.PublicRelationListResponseDto;
import com.example.sideproject.domain.pr.dto.read.PublicRelationReadDto;
import com.example.sideproject.domain.pr.dto.search.SearchPublicRelationRequest;
import com.example.sideproject.domain.pr.entity.PublicRelation;
import com.example.sideproject.domain.pr.entity.PublicRelationTechStacks;
import com.example.sideproject.domain.pr.entity.QPublicRelation;
import com.example.sideproject.domain.pr.entity.QPublicRelationTechStacks;
import com.example.sideproject.domain.user.entity.TechStack;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PublicRelationQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private QPublicRelation qPr = QPublicRelation.publicRelation;
    private QPublicRelationTechStacks qPrTs = QPublicRelationTechStacks.publicRelationTechStacks;

    public Page<PublicRelationListResponseDto> findPublicRelationList(SearchPublicRelationRequest request, Pageable pageable) {

        List<PublicRelationReadDto> publicRelations = jpaQueryFactory.select(Projections.constructor(
                        PublicRelationReadDto.class,
                        qPr.id,
                        qPr.title,
                        qPr.user.username
                )).from(qPr)
                .leftJoin(qPr.user)
                .leftJoin(qPrTs).on(qPr.id.eq(qPrTs.publicRelation.id))
                .where(
                        techStacksAnd(request)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> ids = publicRelations.stream().map(PublicRelationReadDto::id).toList();

        List<PublicRelationTechStacks> techStacks = jpaQueryFactory.selectFrom(qPrTs)
                .where(qPrTs.publicRelation.id.in(ids))
                .fetch();

        Map<Long, List<TechStack>> techStacksMap = techStacks.stream()
                .collect(Collectors.groupingBy(PublicRelationTechStacks::getPublicRelationId,
                        Collectors.mapping(PublicRelationTechStacks::getTechStack, Collectors.toList())));

        List<PublicRelationListResponseDto> result = publicRelations.stream()
                .map(pr -> PublicRelationListResponseDto.builder()
                        .id(pr.id())
                        .title(pr.title())
                        .username(pr.username())
                        .techStacks(techStacksMap.get(pr.id()))
                        .build()
                ).toList();

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery().fetchOne());
    }

    private JPAQuery<Long> countQuery() {
        return jpaQueryFactory.select(qPr.count())
                .from(qPr);
    }

    private BooleanExpression techStacksAnd(SearchPublicRelationRequest request) {
        if (request.stacks().isEmpty()) {
            return null;
        }

        return request.toList().stream()
                .map(this::existsTechStack)
                .reduce(BooleanExpression::and)
                .orElse(null);
    }

    private BooleanExpression existsTechStack(TechStack techStack) {
        return JPAExpressions
                .selectOne()
                .from(qPrTs)
                .where(qPrTs.techStack.eq(techStack)
                        .and(qPrTs.publicRelation.id.eq(qPr.id)))
                .exists();
    }

}
