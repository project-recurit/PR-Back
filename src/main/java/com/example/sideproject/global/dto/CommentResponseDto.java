package com.example.sideproject.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentResponseDto {
    private final Long commentId;
    private final String content;
    private final String nickname;
    private final String profileUrl;
    private final int replyCount;
    private final String createdAt;
    private final String modifiedAt;
}
