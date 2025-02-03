package com.example.sideproject.domain.applicant.dto;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;

public record ApplicantRequestDto(
        ApplicationStatus status
) {

}
