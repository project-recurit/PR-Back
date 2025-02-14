package com.example.sideproject.domain.project.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record ProjectResponseDto(
        Long id,
        String title,
        String content,
        String expectedPeriod,
        int viewCount,
        int likeCount,
        String userNickname,
        String recruitmentPeriod,
        String recruitStatus,
        String teamSize,
        String modifiedAt
) {
}