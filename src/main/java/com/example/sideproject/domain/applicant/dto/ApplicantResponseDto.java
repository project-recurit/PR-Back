package com.example.sideproject.domain.applicant.dto;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.global.enums.Position;

import java.time.LocalDateTime;

public record ApplicantResponseDto(
        Long id,
        Long recruitmentId,
        String nickname,
        Position position,
        ApplicationStatus status,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}
