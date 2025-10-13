package com.example.sideproject.global.util;

import com.example.sideproject.domain.applicant.entity.Applicant;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class QueryUtil {
    public <T, E> Page<T> createPage(JPAQueryFactory factory, EntityPathBase<E> entityPath, List<T> contents, Pageable pageable, BooleanExpression search) {
        JPAQuery<Long> countQuery = countQuery(factory, entityPath, search);
        return PageableExecutionUtils.getPage(contents, pageable, () -> countQuery.fetchOne());
    }

    public <E> OrderSpecifier<?>[] createOrderSpecifiers(Sort sort, EntityPathBase<E> entityPath) {
        Class<? extends E> entityClass = entityPath.getType();
        String instanceName = entityPath.getMetadata().getName();

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<?> expression = new PathBuilder<>(entityClass, instanceName);
            orders.add(new OrderSpecifier<>(direction, expression.get(order.getProperty(), Comparable.class)));
        });
        return orders.toArray(OrderSpecifier[]::new);
    }

    private <E> JPAQuery<Long> countQuery(JPAQueryFactory factory, EntityPathBase<E> entity, BooleanExpression search) {
        return factory.select(entity.count())
                .from(entity)
                .where(search);
    }
}
