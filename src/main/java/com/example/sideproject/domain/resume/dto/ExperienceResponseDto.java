package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Experience;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExperienceResponseDto(
        Long experienceId,
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int teamSize,
        String achievement,
        String documentUrl
) {
    public static ExperienceResponseDto of(Experience experience) {
        return ExperienceResponseDto.builder()
                .experienceId(experience.getId())
                .title(experience.getTitle())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .teamSize(experience.getTeamSize())
                .achievement(experience.getAchievement())
                .documentUrl(experience.getDocumentUrl())
                .build();
    }
}
