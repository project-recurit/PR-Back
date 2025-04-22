package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Experience;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExperienceResponse(
        Long id,
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int teamSize,
        String description,
        String documentUrl
) {
    public static ExperienceResponse of(Experience experience) {
        return ExperienceResponse.builder()
                .id(experience.getId())
                .title(experience.getTitle())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .teamSize(experience.getTeamSize())
                .description(experience.getDescription())
                .documentUrl(experience.getDocumentUrl())
                .build();
    }
}
