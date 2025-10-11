package com.example.sideproject.domain.status.project.dto;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.recruitment.dto.RecruitmentsResponseDto;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.dto.TechStackResponse;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class StatusApplicantResponseDto implements TechStackResponse {
    @JsonUnwrapped
    private RecruitmentsResponseDto recruitment;
    @Schema(description = "지원 상태")
    private ApplicationStatus status;

    public StatusApplicantResponseDto(RecruitmentsResponseDto recruitment, ApplicationStatus status) {
        this.recruitment =  recruitment;
        this.status = status;
    }

    @Override
    public StatusApplicantResponseDto setTechStacks(List<TechStackDto> techStacks) {
        recruitment.setTechStacks(techStacks);
        return this;
    }

    @Override
    public Long getTargetId() {
        return recruitment.getId();
    }
}
