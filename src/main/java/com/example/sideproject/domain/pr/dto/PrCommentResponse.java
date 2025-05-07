package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PrComment;
import com.example.sideproject.global.dto.CommentResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PrCommentResponse extends CommentResponseDto {
    public PrCommentResponse(Long commentId, String content, String nickname, String profileUrl, int replyCount, LocalDateTime createdAt,
                             LocalDateTime modifiedAt) {
        super(commentId, content, nickname, profileUrl, replyCount, String.valueOf(createdAt), String.valueOf(modifiedAt));
    }

    public PrCommentResponse(PrComment prComment) {
        super(prComment.getId(), prComment.getContent(), prComment.getUser().getNickname(), prComment.getUser().getProfileUrl(),
              prComment.getReplyCount(), prComment.getCreatedAt(), prComment.getModifiedAt());
    }
}
