package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.PrComment;
import com.example.sideproject.domain.user.entity.User;

public record PrCommentRequest(
        Long parentId,
        String content
) {
    public PrComment toEntity(User user, Pr pr, PrComment parent) {
        return PrComment.builder()
                .pr(pr)
                .parent(parent)
                .user(user)
                .content(content)
                .build();
    }
}
