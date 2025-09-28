package com.example.sideproject.domain.status.project.dto;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.recruitment.dto.RecruitmentsResponseDto;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class StatusApplicantResponseDto {
    @JsonUnwrapped
    private RecruitmentsResponseDto recruitment;
    @Schema(description = "지원 상태")
    private ApplicationStatus status;

    public StatusApplicantResponseDto(RecruitmentsResponseDto recruitment, ApplicationStatus status) {
        this.recruitment =  recruitment;
        this.status = status;
    }

    public StatusApplicantResponseDto setTechStacks(List<TechStackDto> techStacks) {
        recruitment.setTechStacks(techStacks);
        return this;
    }
}
