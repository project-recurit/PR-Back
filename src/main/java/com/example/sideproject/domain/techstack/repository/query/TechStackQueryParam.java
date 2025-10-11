package com.example.sideproject.domain.techstack.repository.query;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.dto.TechStackMappingDto;
import com.example.sideproject.domain.techstack.dto.TechStackResponse;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import lombok.Builder;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Builder
public record TechStackQueryParam<T extends TechStackMappingDto>(
        NumberPath<Long> idPath,
        EntityPathBase<?> entityPath,
        EntityPathBase<?> joinPath,
        Class<T> mappingClass,
        List<Expression<?>> selectExpressions,
        Function<T, TechStackDto> resultMapper
) {

    @SuppressWarnings("unchecked")
    public TechStackQueryParam {
        // 기본 값은 TechStackMappingDto
        // TechStackMappingDto를 확장 하는 경우 반드시 mappingClass, resultMapper 설정 필요
        if (mappingClass == null) {
            mappingClass = (Class<T>) TechStackMappingDto.class;
        }

        if (resultMapper == null) {
            resultMapper = TechStackMappingDto::toTechStackDto;
        }
    }

    public Expression<T> getSelectExpression() {
        return Projections.constructor(
                mappingClass,
                selectExpressions.toArray(new Expression[0])
        );
    }
}
