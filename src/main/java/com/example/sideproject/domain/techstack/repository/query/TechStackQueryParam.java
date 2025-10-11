package com.example.sideproject.domain.techstack.repository.query;

import com.example.sideproject.domain.techstack.dto.BasicTechStack;
import com.example.sideproject.domain.techstack.dto.TechStackMapping;
import com.example.sideproject.domain.techstack.dto.TechStackVo;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import io.jsonwebtoken.lang.Assert;
import lombok.Builder;

import java.util.List;
import java.util.function.Function;

/**
 * 기술 스택 조회를 위한 객체
 * @param idPath in절 조회를 위한 idPath
 * @param entityPath 기술스택 entityPath
 * @param joinPath join을 위한 entityPath
 * @param mappingClass 기술 스택 조회 시 매핑을 위한 클래스 타입, selectExpressions에 해당해야 함
 * @param selectExpressions 기술 스택 조회를 위한 select 필드, 반드시 mappingClass와 순서가 맞아야 함
 * @param resultMapper mapping 객체에서 변경할 TechStackMapping 리턴 타입 함수
 * @param <T> mapping 기술 스택 조회 시 매핑을 위한 타입
 */
@Builder
public record TechStackQueryParam<T extends BasicTechStack>(
        NumberPath<Long> idPath,
        EntityPathBase<?> entityPath,
        EntityPathBase<?> joinPath,
        Class<? extends T> mappingClass,
        List<Expression<?>> selectExpressions,
        Function<T, TechStackMapping> resultMapper
) {

    @SuppressWarnings("unchecked")
    public TechStackQueryParam {
        Assert.notNull(idPath, "idPath is required");
        Assert.notNull(entityPath, "entityPath is required");
        Assert.notNull(selectExpressions, "selectExpressions are required");
        Assert.notNull(joinPath, "joinPath is required");

        // 기본 값은 TechStackMappingDto
        // TechStackMappingDto를 확장 하는 경우 반드시 mappingClass, resultMapper 설정 필요
        if (mappingClass == null) {
            mappingClass = (Class<T>) TechStackVo.class;
        }

        if (resultMapper == null) {
            resultMapper = TechStackVo::toBasicDto;
        }
    }

    public Expression<T> getSelectExpression() {
        return Projections.constructor(
                mappingClass,
                selectExpressions.toArray(new Expression[0])
        );
    }
}
