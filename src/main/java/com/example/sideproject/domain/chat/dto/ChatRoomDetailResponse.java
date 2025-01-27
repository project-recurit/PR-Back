package com.example.sideproject.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ChatRoomDetailResponse {
    private Long roomId;
    private List<ChatMessageResponse> messages;
    private ChatMessageResponse lastMessage;
    private List<ChatRoomMemberResponse> members;
    private LocalDateTime createdAt;
}
