package com.example.sideproject.domain.project.dto;

public record ProjectsQueryDto(
        Long id,
        String title,
        String userNickname,
        int viewCount,
        int likeCount,
        String modifiedAt
) {
}
