package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.chat.entity.ChatMessage;
import com.example.sideproject.domain.chat.entity.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

public record ChatMessageResponse(
        Long messageId,
        Long roomId,
        Long senderId,
        String senderNickname,
        String content,
        LocalDateTime sentAt,
        MessageType type,
        Map<Long, Long> unreadCounts,
        boolean read
) {
    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getChatRoom().getId(),
                message.getSender().getId(),
                message.getSender().getNickname(),
                message.getContent(),
                message.getSentAt(),
                message.getType(),
                null,  // unreadCounts는 기본적으로 null
                message.isRead()
        );
    }

    // unreadCounts를 포함한 메시지 생성을 위한 별도 팩토리 메서드
    public static ChatMessageResponse fromWithUnreadCounts(ChatMessage message, Map<Long, Long> unreadCounts) {
        return new ChatMessageResponse(
                message.getId(),
                message.getChatRoom().getId(),
                message.getSender().getId(),
                message.getSender().getNickname(),
                message.getContent(),
                message.getSentAt(),
                message.getType(),
                unreadCounts,
                message.isRead()
        );
    }
}
