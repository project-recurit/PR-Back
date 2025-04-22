package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.pr.dto.PrCommentListResponse;
import com.example.sideproject.domain.pr.dto.PrCommentRequest;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.PrComment;
import com.example.sideproject.domain.pr.repository.PrCommentRepository;
import com.example.sideproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
