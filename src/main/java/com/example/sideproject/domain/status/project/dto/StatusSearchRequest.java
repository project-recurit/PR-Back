package com.example.sideproject.domain.status.project.dto;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.global.dto.DateSort;
import com.example.sideproject.global.dto.PageDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;

import java.util.Objects;

@ParameterObject
public record StatusSearchRequest(
        @Parameter(description = "날짜 정렬", schema = @Schema(implementation = DateSort.class))
        DateSort dateSort,

        @Parameter(description = "지원 상태", schema = @Schema(implementation = ApplicationStatus.class))
        ApplicationStatus status,

        @Parameter(description = "페이지")
        PageDto pageDto
) {
    public StatusSearchRequest {
        pageDto = Objects.requireNonNullElse(pageDto, PageDto.create());

        if (dateSort == null) {
            dateSort = DateSort.basicSort();
        }
    }
}
