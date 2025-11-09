package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 구인공고 검색 DTO
 * 관련 클래스
 * 1. RecruitmentTechStack.class
 * 2. RecruitmentPosition.class
 * 3. WorkType.enum
 */
@Getter
@AllArgsConstructor
@Builder
public class RecruitmentSearchDto {
    @Parameter(description = "기술스택 고유번호, ','를 이용하여 여러개 작성",
            array = @ArraySchema(schema = @Schema(implementation = Long.class)),
            explode = Explode.FALSE)
    private List<Long> techStacks = new ArrayList<>();

    @Parameter(description = "포지션, ','를 이용하여 여러개 작성",
            array = @ArraySchema(schema = @Schema(implementation = Position.class)),
            explode = Explode.FALSE)
    private List<Position> positions = new ArrayList<>();

    @Parameter(description = "진행 방식, ','를 이용하여 여러개 작성",
            array = @ArraySchema(schema = @Schema(implementation = WorkType.class)),
            explode = Explode.FALSE)
    private List<WorkType> workType = new ArrayList<>();

    public RecruitmentSearchDto(){}
}
