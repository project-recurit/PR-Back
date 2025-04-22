package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.pr.dto.PrCommentListResponse;
import com.example.sideproject.domain.pr.dto.PrCommentRequest;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.PrComment;
import com.example.sideproject.domain.pr.repository.PrCommentRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrCommentService {
    private final PrCommentRepository prCommentRepository;

    public Long saveComment(User user, Long prId, PrCommentRequest prCommentRequest) {
        Pr pr = new Pr(prId);
        PrComment prComment = prCommentRequest.toEntity(user, pr);
        return prCommentRepository.save(prComment).getId();
    }

    public List<PrCommentListResponse> getComment(Long prId) {
        List<PrComment> comments = prCommentRepository.findByPr_IdAndParentIsNull(prId);
        return comments.stream().map(PrCommentListResponse::new).toList();
    }

    @Transactional
    public Long updateComment(User user, Long commentId, PrCommentRequest prCommentRequest) {
        PrComment prComment = getPrComment(commentId);
        if (!prComment.isOwner(user.getId())) {
            throw new CustomException(ErrorType.NOT_OWNER);
        }
        String content = prCommentRequest.content();
        prComment.contentUpdate(content);
        return prComment.getId();
    }

    public PrComment getPrComment(Long id) {
        return prCommentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorType.PR_COMMENT_NOT_FOUND)
        );
    }

    public void deletetComment(User user, Long commentId) {
        PrComment prComment = getPrComment(commentId);
        if (!prComment.isOwner(user.getId())) {
            throw new CustomException(ErrorType.NOT_OWNER);
        }
        prCommentRepository.delete(prComment);
    }
}
