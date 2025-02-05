package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Experience;
import lombok.Builder;

@Builder
public record ExperienceResponseDto(
        Long experienceId,
        String title,
        String period,
        int teamSize,
        String achievement,
        String documentUrl
) {
    public static ExperienceResponseDto of(Experience experience) {
        return ExperienceResponseDto.builder()
                .experienceId(experience.getId())
                .title(experience.getTitle())
                .period(experience.getPeriod())
                .teamSize(experience.getTeamSize())
                .achievement(experience.getAchievement())
                .documentUrl(experience.getDocumentUrl())
                .build();
    }
}
