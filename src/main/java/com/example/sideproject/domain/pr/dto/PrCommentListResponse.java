package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PrComment;
import lombok.Getter;

import java.util.List;

@Getter
public class PrCommentListResponse {
    private final Long id;
    private final User user;
    private final String content;
    private List<Reply> reply;
    private final String modifiedAt;
    private final String createdAt;

    public PrCommentListResponse(
            Long id,
            String content,
            String modifiedAt,
            String createdAt,
            String nickname,
            String profileUrl
    ) {
        this.id = id;
        this.content = content;
        this.modifiedAt = modifiedAt;
        this.createdAt = createdAt;
        this.user = new User(nickname, profileUrl);
    }

    public PrCommentListResponse(PrComment prComment) {
        this.id = prComment.getId();
        this.content = prComment.getContent();
        this.modifiedAt = prComment.getModifiedAt();
        this.createdAt = prComment.getCreatedAt();
        this.user = new User(prComment.getUser().getNickname(), prComment.getUser().getProfileUrl());
        this.reply = prComment.getReply().stream().map(Reply::new).toList();
    }


    record Reply(
            Long id,
            User user,
            String content,
            String modifiedAt,
            String createdAt
    ) {
        public Reply(PrComment reply) {
            this(
                    reply.getId(),
                    new User(reply.getUser().getNickname(), reply.getUser().getProfileUrl()),
                    reply.getContent(),
                    reply.getModifiedAt(),
                    reply.getCreatedAt()
            );
        }
    }

    record User(
            String nickname,
            String profileUrl
    ) {
    }
}
