package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.domain.recruitment.entity.RecruitmentComment;
import com.example.sideproject.global.dto.CommentResponseDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecruitmentCommentResponseDto {

    private Long commentId;
    private String content;
    private String nickname;
    private String profileUrl;
    private int replyCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public RecruitmentCommentResponseDto() {}

    /**
     * QueryDSL용 생성자
     */
    @QueryProjection
    public RecruitmentCommentResponseDto(Long commentId,
                                         String content,
                                         String nickname,
                                         String profileUrl,
                                         int replyCount,
                                         LocalDateTime createdAt,
                                         LocalDateTime modifiedAt) {
        this.commentId = commentId;
        this.content = content;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.replyCount = replyCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}