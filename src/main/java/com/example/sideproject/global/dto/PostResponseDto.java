package com.example.sideproject.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String nickname;
    private final String profileUrl;
    private final int viewCount;
    private final int commentCount;
    private final int favoriteCount;
    private final String createdAt;
    private final String modifiedAt;
}
