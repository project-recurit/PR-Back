package com.example.sideproject.domain.applicant.dto;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;

public record ApplicantResponseDto(
        Long id,
        Long projectId,
        String nickname,
        ApplicationStatus status,
        String createdAt,
        String modifiedAt
) {
}
