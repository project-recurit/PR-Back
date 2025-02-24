package com.example.sideproject.domain.comment.dto;

import java.util.List;

public record NestedCommentDto(
        CommentResponseDto parentComment,
        List<CommentResponseDto> childComments
) {
}
