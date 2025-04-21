package com.example.sideproject.domain.pr.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PrExperienceResponse(
        Long id,
        String title,
        String description,
        int teamSize,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String documentUrl
) {
}
