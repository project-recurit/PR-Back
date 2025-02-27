package com.example.sideproject.domain.comment.service;

import com.example.sideproject.domain.comment.dto.CommentRequestDto;
import com.example.sideproject.domain.comment.dto.CommentResponseDto;
import com.example.sideproject.domain.comment.entity.Comment;
import com.example.sideproject.domain.comment.repository.CommentRepository;
import com.example.sideproject.domain.comment.repository.query.CommentQueryRepository;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.service.ProjectService;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Page<CommentResponseDto> getComments(Long projectId, int page) {

        final Pageable pageable = PageRequest.of(page - 1, 20);
        return commentQueryRepository.getComments(projectId, pageable);
    }

    /**
     * 대댓글 전체 조회
     */
    public List<CommentResponseDto> getReply(Long commentId) {

        return commentQueryRepository.getReply(commentId);
    }

    /**
     * http 메서드 patch 사용
     * 이유: 바꿀 컬럼이 content밖에 없어서 다른거 다 안받아와도 될거같음
     */
    @Transactional
    public void updateComment(Long commentId, User user, CommentRequestDto requestDto) {

        final Comment comment = findComment(commentId);

        if(user.getId() != comment.getUser().getId()) {
            throw new CustomException(ErrorType.NOT_USER_COMMENT);
        }

        comment.update(requestDto.content());
    }

    /**
     * 댓글 삭제
     * commentId나 user의 값이 하나라도 일치 안하면 에러
     */
    public void deleteComment(Long commentId, User user) {

        final Comment comment = findComment(commentId);

        if(user.getId() != comment.getUser().getId()) {
            throw new CustomException(ErrorType.NOT_USER_COMMENT);
        }

        commentRepository.deleteById(commentId);
    }

    public Comment findComment(Long commentId) { // 댓글 가져오는 메서드
        return commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_COMMENT));
    }
}
