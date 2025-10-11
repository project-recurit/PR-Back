package com.example.sideproject.domain.techstack.repository.query;

import com.example.sideproject.domain.techstack.dto.*;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TechStackQueryRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * 조회 결과에 각 id에 해당하는 기술 스택 데이터를 넣어 반환한다.
     * @param param 기술 스택을 조회하기 위한 정보
     * @param queryResults 기술 스택 데이터를 넣을 조회 결과
     * @return 기술 스택 데이터를 추가한 조회 결과
     * @param <T> 기술 스택 조회를 위한 매핑 객체 타입
     * @param <R> 리턴 타입
     */
    public <T extends BasicTechStack, R extends TechStackResponse> List<R> withTechStacks(
            TechStackQueryParam<T> param,
            List<R> queryResults
    ) {
        Map<Long, List<TechStackMapping>> techStackMap = getTechStackMap(
                param.idPath(),
                getIds(queryResults),
                param.getSelectExpression(),
                param.entityPath(),
                param.joinPath(),
                param.resultMapper()
        );

        queryResults.forEach(it -> it.setTechStacks(techStackMap.get(it.getTargetId())));

        return queryResults;
    }

    private <T extends BasicTechStack> Map<Long, List<TechStackMapping>> getTechStackMap(
            NumberPath<Long> idPath,
            List<Long> ids,
            Expression<T> selectExpression,
            EntityPathBase<?> entityPath,
            EntityPathBase<?> joinPath,
            Function<T, TechStackMapping> resultMapper
    ) {
        List<T> techStacks = queryFactory.select(selectExpression)
                .from(entityPath)
                .join(joinPath)
                .where(idPath.in(ids))
                .fetch();

        return techStacks.stream()
                .collect(Collectors.groupingBy(
                        BasicTechStack::id,
                        Collectors.mapping(resultMapper, Collectors.toList())
                ));
    }

    private <R extends TechStackResponse> List<Long> getIds(List<R> queryResults) {
        return queryResults.stream()
                .map(TechStackResponse::getTargetId)
                .toList();
    }
}
