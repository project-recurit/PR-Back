package com.example.sideproject.domain.status.project.dto;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.global.dto.DateSort;
import com.example.sideproject.global.dto.PageDto;
import com.example.sideproject.global.dto.SearchDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;

import java.util.Objects;

@ParameterObject
public record StatusSearchRequest(
        SearchDto searchDto,

        @Parameter(description = "지원 상태", schema = @Schema(implementation = ApplicationStatus.class))
        ApplicationStatus status
) {
    public StatusSearchRequest {
        searchDto = Objects.requireNonNullElse(searchDto, SearchDto.create());
    }
}
