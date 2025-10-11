package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.global.dto.SearchDto;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@ParameterObject
public record PrSearchRequest(
        SearchDto searchDto,

        @Parameter(description = "포지션, ','를 이용하여 여러개 작성",
                array = @ArraySchema(schema = @Schema(implementation = Position.class)),
                explode = Explode.FALSE)
        List<Position> positions,

        @Parameter(description = "기술스택 고유번호, ','를 이용하여 여러개 작성",
                array = @ArraySchema(schema = @Schema(implementation = Long.class)),
                explode = Explode.FALSE
        )
        List<Long> techStackIds,

        @Parameter(description = "진행 방식, ','를 이용하여 여러개 작성",
                array = @ArraySchema(schema = @Schema(implementation = WorkType.class)),
                explode = Explode.FALSE
        )
        List<WorkType> workTypes,

        @Parameter(description = "검색 텍스트")
        String searchText
) {
    public PrSearchRequest {
        searchDto = Objects.requireNonNullElse(searchDto, SearchDto.create());
        // 중복 제거
        if (positions == null) {
            positions = new ArrayList<>();
        }

        if (techStackIds == null) {
            techStackIds = new ArrayList<>();
        }

        if (workTypes == null) {
            workTypes = new ArrayList<>();
        }

        positions = new HashSet<>(positions).stream().toList();
        techStackIds = new HashSet<>(techStackIds).stream().toList();
        workTypes = new HashSet<>(workTypes).stream().toList();
    }
}
