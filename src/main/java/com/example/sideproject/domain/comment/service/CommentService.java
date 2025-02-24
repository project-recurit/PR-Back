package com.example.sideproject.domain.comment.service;

import com.example.sideproject.domain.comment.dto.CommentRequestDto;
import com.example.sideproject.domain.comment.entity.Comment;
import com.example.sideproject.domain.comment.repository.CommentRepository;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.service.ProjectService;
import com.example.sideproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProjectService projectService;

    /**
     * 댓글 생성
     * 파라미터에 parentId 값 x  -> 댓글
     * 파라미터에 parentId 값 o  -> 대댓글
     */
    public void createComment(Long projectId, User user, CommentRequestDto requestDto) {

        final Project project = projectService.findProject(projectId);
        final Comment comment = requestDto.toEntity(user, project);
        commentRepository.save(comment);
    }
}
