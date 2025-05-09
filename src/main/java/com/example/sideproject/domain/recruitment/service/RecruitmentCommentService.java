package com.example.sideproject.domain.recruitment.service;

import com.example.sideproject.domain.recruitment.dto.RecruitmentCommentRequestDto;
import com.example.sideproject.domain.recruitment.dto.RecruitmentCommentResponseDto;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentComment;
import com.example.sideproject.domain.recruitment.repository.RecruitmentCommentRepository;
import com.example.sideproject.domain.recruitment.repository.query.RecruitmentCommentQueryRepository;
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
public class RecruitmentCommentService {

    private final RecruitmentCommentRepository commentRepository;
    private final RecruitmentService recruitmentService;
    private final RecruitmentCommentQueryRepository commentQueryRepository;

    /**
     * 댓글 생성
     * 파라미터에 parentId 값 x  -> 댓글
     * 파라미터에 parentId 값 o  -> 대댓글
     */
    @Transactional
    public void createComment(Long recruitmentId, User user, RecruitmentCommentRequestDto requestDto) {

        final Recruitment recruitment = recruitmentService.findRecruitment(recruitmentId);
        final RecruitmentComment comment = requestDto.toEntity(user, recruitment);

        if(requestDto.parentId() != null) {
            RecruitmentComment parent = findComment(requestDto.parentId());
            parent.increaseReplyCount();
        }
        recruitment.addCommentCount();

        commentRepository.save(comment);
    }

    /**
     * 댓글 조회
     * 상세조회는 필요 없어서 바로 전체 조회
     */
    public Page<RecruitmentCommentResponseDto> getComments(Long recruitmentId, int page) {
        final Pageable pageable = PageRequest.of(page - 1, 20);
        return commentQueryRepository.getComments(recruitmentId, pageable);
    }

    /**
     * 대댓글 전체 조회
     */
    public List<RecruitmentCommentResponseDto> getReply(Long commentId) {
        return commentQueryRepository.getReply(commentId);
    }

    /**
     * http 메서드 patch 사용
     * 이유: 바꿀 컬럼이 content밖에 없어서 다른거 다 안받아와도 될거같음
     */
    @Transactional
    public void updateComment(Long commentId, User user, RecruitmentCommentRequestDto requestDto) {

        final RecruitmentComment comment = findComment(commentId);

        if(user.getId() != comment.getUser().getId()) {
            throw new CustomException(ErrorType.NOT_USER_COMMENT);
        }

        comment.update(requestDto.content());
    }

    /**
     * 댓글 삭제
     * commentId나 user의 값이 하나라도 일치 안하면 에러
     */
    @Transactional
    public void deleteComment(Long commentId, User user) {

        final RecruitmentComment comment = findComment(commentId);

        if(user.getId() != comment.getUser().getId()) {
            throw new CustomException(ErrorType.NOT_USER_COMMENT);
        }
        comment.getRecruitment().downCommentCount();

        if(comment.getParentId() != null) {
            RecruitmentComment parentComment = findComment(comment.getParentId());
            parentComment.decreaseReplyCount(); // 부모댓글 대댓글 카운트 1감소
        }
        commentRepository.deleteById(commentId);
    }

    public RecruitmentComment findComment(Long commentId) { // 댓글 가져오는 메서드
        return commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_COMMENT));
    }
}