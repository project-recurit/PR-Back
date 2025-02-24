package com.example.sideproject.domain.comment.dto;

import com.example.sideproject.domain.comment.entity.Comment;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.user.entity.User;

public record CommentRequestDto(
        Long parentId,
        String content
) {

    public Comment toEntity(User user, Project project) {
        return Comment.builder()
                .content(content)
                .parentId(parentId)
                .user(user)
                .project(project)
                .build();
    }
}
