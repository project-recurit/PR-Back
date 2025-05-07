package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PrComment;
import com.example.sideproject.global.dto.CommentResponseDto;
import lombok.Getter;

@Getter
public class PrCommentResponse extends CommentResponseDto {
    public PrCommentResponse(Long commentId, String content, String nickname, String profileUrl, int replyCount, String createdAt, String modifiedAt) {
        super(commentId, content, nickname, profileUrl, replyCount, createdAt, modifiedAt);
    }

    public PrCommentResponse(PrComment prComment) {
        super(prComment.getId(), prComment.getContent(), prComment.getUser().getNickname(), prComment.getUser().getProfileUrl(), prComment.getReplyCount(), prComment.getCreatedAtToString(), prComment.getModifiedAtToString());
    }
}
