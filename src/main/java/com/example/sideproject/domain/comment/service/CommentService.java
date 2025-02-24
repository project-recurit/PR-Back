package com.example.sideproject.domain.comment.service;

import com.example.sideproject.domain.comment.dto.CommentRequestDto;
import com.example.sideproject.domain.comment.dto.CommentResponseDto;
import com.example.sideproject.domain.comment.dto.NestedCommentDto;
import com.example.sideproject.domain.comment.entity.Comment;
import com.example.sideproject.domain.comment.repository.CommentRepository;
import com.example.sideproject.domain.comment.repository.query.CommentQueryRepository;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.service.ProjectService;
import com.example.sideproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProjectService projectService;
    private final CommentQueryRepository commentQueryRepository;

    /**
     * 댓글 생성
     * 파라미터에 parentId 값 x  -> 댓글
     * 파라미터에 parentId 값 o  -> 대댓글
     */
    public void createComment(Long projectId, User user, CommentRequestDto requestDto) {

        final Project project = projectService.findProject(projectId);
        final Comment comment = requestDto.toEntity(user, project);
        project.addCommentCount();
        commentRepository.save(comment);
    }

    /**
     * 댓글 조회
     * 상세조회는 필요 없어서 바로 전체 조회
     */
    public Page<NestedCommentDto> getComments(Long projectId, int page) {
        final Pageable pageable = PageRequest.of(page - 1, 20);
        return commentQueryRepository.getComments(projectId, pageable);
    }
}
