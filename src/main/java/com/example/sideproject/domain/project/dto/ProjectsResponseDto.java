package com.example.sideproject.domain.project.dto;

import java.util.List;

public record ProjectsResponseDto(
        Long id,
        String title,
        String userNickname,
        int viewCount,
        int likeCount,
        String modifiedAt,
        List<ProjectTechStackResponseDto> techStacks
) {
}
