package com.example.sideproject.domain.applicant.dto.search;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.global.enums.Position;

public record SearchApplicantDto(
        String nickname,
        Position position,
        ApplicationStatus status
) {
}
