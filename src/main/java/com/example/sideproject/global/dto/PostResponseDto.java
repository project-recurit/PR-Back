package com.example.sideproject.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

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

    public PostResponseDto(Long id, String title, String nickname, String profileUrl, int viewCount, int commentCount, int favoriteCount,
                           LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
        this.createdAt = createdAt == null? "" : createdAt.toString();
        this.modifiedAt = modifiedAt == null? "" : modifiedAt.toString();
    }
}
