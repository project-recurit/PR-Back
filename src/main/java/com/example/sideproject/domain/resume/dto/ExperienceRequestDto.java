package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Experience;

public record ExperienceRequestDto(
        String title,
        String period,
        int teamSize,
        String achievement,
        String documentUrl
) {
    public Experience toEntity() {
        return Experience.builder()
                .title(title)
                .period(period)
                .teamSize(teamSize)
                .achievement(achievement)
                .documentUrl(documentUrl)
                .build();
    }
}
