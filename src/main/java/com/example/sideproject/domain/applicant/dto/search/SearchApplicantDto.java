package com.example.sideproject.domain.applicant.dto.search;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;

public record SearchApplicantDto(
        String nickname,
        String position,
        ApplicationStatus status
) {
}
