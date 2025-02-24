package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.project.entity.Project;

public record ProjectSummaryResponse(
        Long id,
        String title
) {
    public static ProjectSummaryResponse from(Project project) {
        return new ProjectSummaryResponse(
                project.getId(),
                project.getTitle()
        );
    }
}
