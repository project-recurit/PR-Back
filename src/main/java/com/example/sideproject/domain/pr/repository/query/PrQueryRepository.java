package com.example.sideproject.domain.pr.repository.query;

import com.example.sideproject.domain.pr.dto.PrListResponseDto;
import com.example.sideproject.domain.pr.dto.PrSearchRequest;
import com.example.sideproject.domain.pr.dto.PrTechStackMapping;
import com.example.sideproject.domain.pr.dto.PrTechStackResponse;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.QPr;
import com.example.sideproject.domain.pr.entity.QPrTechStack;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
public class PrQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QPr qPr = QPr.pr;

    public Page<PrListResponseDto> getPrs(Pageable pageable, PrSearchRequest prSearchRequest) {
        List<PrListResponseDto> prs = jpaQueryFactory.select(Projections.constructor(
                        PrListResponseDto.class,
                        qPr.id,
                        qPr.title,
                        qPr.user.nickname,
                        qPr.user.profileUrl,
                        qPr.count.viewCount,
                        qPr.count.commentCount,
                        qPr.count.favoriteCount,
                        qPr.createdAt,
                        qPr.modifiedAt,
                        qPr.workType,
                        qPr.position
                )).from(qPr)
                .where(prSearchCondition(prSearchRequest))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .fetch();

        List<Long> prIds = prs.stream()
                .map(PrListResponseDto::getId)
                .toList();

        List<PrTechStackMapping> techStacks = getTechStacks(prIds);

        Map<Long, List<PrTechStackResponse>> techStackMap = techStacks.stream().collect(
                Collectors.groupingBy(
                        PrTechStackMapping::prId,
                        Collectors.mapping(
                                PrTechStackResponse::new,
                                Collectors.toList()
                        )
                )
        );

        List<PrListResponseDto> result = prs.stream()
                .map(pr -> pr.addTechStacks(techStackMap.get(pr.getId()))).toList();

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery(prSearchCondition(prSearchRequest)).fetchOne());
    }

    private BooleanExpression prSearchCondition(PrSearchRequest prSearchRequest) {
        List<Position> positions = prSearchRequest.positions();
        List<Long> techStackIds = prSearchRequest.techStackIds();
        List<WorkType> workTypes = prSearchRequest.workTypes();

        BooleanExpression positionIn = null;
        BooleanExpression workTypeIn = null;
        BooleanExpression idIn = null;

        if (!positions.isEmpty()) {
            positionIn = qPr.position.in(positions);
        }

        if (!workTypes.isEmpty()) {
            workTypeIn = qPr.workType.in(workTypes);
        }

        List<Long> prIds = findIdsByTechStackIds(techStackIds);
        if (prIds != null) {
            idIn = qPr.id.in(prIds);
        }

        return Expressions.allOf(positionIn, workTypeIn, idIn, containsBySearchText(prSearchRequest.searchText()));
    }

    private BooleanExpression containsBySearchText(String searchText){
        if (searchText == null) {
            return null;
        }
        return qPr.title.contains(searchText).or(qPr.introduce.contains(searchText));
    }

    private List<Long> findIdsByTechStackIds(List<Long> techStackIds) {
        if (techStackIds.isEmpty()) {
            return null;
        }

        QPrTechStack qPrTechStack = QPrTechStack.prTechStack;

        return jpaQueryFactory.selectDistinct(qPrTechStack.pr.id)
                .from(qPrTechStack)
                .where(qPrTechStack.techStack.id.in(techStackIds))
                .fetch();
    }

    private List<PrTechStackMapping> getTechStacks(List<Long> prIds) {
        QPrTechStack qPrTechStack = QPrTechStack.prTechStack;

        List<PrTechStackMapping> techStacks = jpaQueryFactory.select(Projections.constructor(
                        PrTechStackMapping.class,
                        qPrTechStack.pr.id,
                        qPrTechStack.techStack.id,
                        qPrTechStack.techStack.name,
                        qPrTechStack.level
                ))
                .from(qPrTechStack)
                .where(
                        qPrTechStack.pr.id.in(prIds)
                )
                .fetch();
        return techStacks;
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<?> expression = new PathBuilder<>(Pr.class, "pr");
            orders.add(new OrderSpecifier<>(direction, expression.get(order.getProperty(), Comparable.class)));
        });
        return orders.toArray(OrderSpecifier[]::new);
    }

    private JPAQuery<Long> countQuery(BooleanExpression searchExpression) {
        return jpaQueryFactory.select(qPr.count())
                .from(qPr)
                .where(searchExpression);
    }
}
