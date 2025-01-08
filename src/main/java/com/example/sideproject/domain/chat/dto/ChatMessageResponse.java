package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String content;

    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getChatRoom().getId(),
                message.getSender().getId(),
                message.getContent()
        );
    }
}
