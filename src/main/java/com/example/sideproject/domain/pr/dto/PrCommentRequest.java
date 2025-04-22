package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PrComment;
import com.example.sideproject.domain.user.entity.User;

public record PrCommentRequest(
        Long parentId,
        String content
) {
    public PrComment toEntity(User user) {
        PrComment parent = null;
        if (parentId != null) {
            parent = new PrComment(parentId);
        }
        return PrComment.builder()
                .parent(parent)
                .user(user)
                .content(content)
                .build();
    }
}
