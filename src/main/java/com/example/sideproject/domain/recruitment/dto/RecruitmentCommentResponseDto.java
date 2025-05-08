package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.domain.recruitment.entity.RecruitmentComment;
import com.example.sideproject.global.dto.CommentResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecruitmentCommentResponseDto extends CommentResponseDto {
    public RecruitmentCommentResponseDto(Long commentId, String content, String nickname, String profileUrl,
                                         int replyCount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        super(commentId, content, nickname, profileUrl, replyCount, String.valueOf(createdAt), String.valueOf(modifiedAt));
    }
//
//    public RecruitmentCommentResponseDto(RecruitmentComment recruitmentComment) {
//        super(
//                recruitmentComment.getId(),
//                recruitmentComment.getContent(),
//                recruitmentComment.getUser().getNickname(),
//                recruitmentComment.getUser().getProfileUrl(),
//                recruitmentComment.getReplyCount(),
//                recruitmentComment.getCreatedAt().toString(),
//                recruitmentComment.getModifiedAt().toString()
//        );
//    }
}
