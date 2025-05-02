package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.chat.entity.ChatRoomType;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.recruitment.entity.Recruitment;

public record ContentSummaryResponse(
        Long id,
        String title,
        ChatRoomType type
) {
    // 단일 팩토리 메서드로 통합
    public static ContentSummaryResponse from(Object content) {
        if (content instanceof Recruitment recruitment) {
            return new ContentSummaryResponse(
                    recruitment.getId(),
                    recruitment.getTitle(),
                    ChatRoomType.PROJECT
            );
        } else if (content instanceof Pr pr) {
            return new ContentSummaryResponse(
                    pr.getId(),
                    pr.getTitle(),
                    ChatRoomType.PR
            );
        }
        throw new IllegalArgumentException("Unsupported content type: " +
                (content != null ? content.getClass().getName() : "null"));
    }
}
