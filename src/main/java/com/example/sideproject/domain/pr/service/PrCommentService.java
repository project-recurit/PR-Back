package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.pr.dto.PrCommentRequest;
import com.example.sideproject.domain.pr.entity.PrComment;
import com.example.sideproject.domain.pr.repository.PrCommentRepository;
import com.example.sideproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrCommentService {
    private final PrCommentRepository prCommentRepository;

    public Long saveComment(User user, PrCommentRequest prCommentRequest) {
        PrComment prComment = prCommentRequest.toEntity(user);
        return prCommentRepository.save(prComment).getId();
    }
}
