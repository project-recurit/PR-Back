package com.example.sideproject.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {
    private final Long commentId;
    private final String content;
    private final String nickname;
    private final String modifiedAt;
    private Long parentId;

    public CommentResponseDto(Long commentId, String content, String nickname, String modifiedAt) {
        this.commentId = commentId;
        this.content = content;
        this.nickname = nickname;
        this.modifiedAt = modifiedAt;
    }

    public CommentResponseDto(Long commentId, String content, String nickname, String modifiedAt, Long parentId) {
        this.commentId = commentId;
        this.content = content;
        this.nickname = nickname;
        this.modifiedAt = modifiedAt;
        this.parentId = parentId;
    }
}
