package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.chat.entity.ChatRoom;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ChatRoomListResponse(
        Long roomId,
        ChatMessageResponse lastMessage,
        List<ChatRoomMemberResponse> members,
        LocalDateTime createdAt,
        long unreadCount
) {
    public static ChatRoomListResponse from(ChatRoom chatRoom, long unreadCount) {
        return new ChatRoomListResponse(
                chatRoom.getId(),
                chatRoom.getLastMessage() != null ? ChatMessageResponse.from(chatRoom.getLastMessage()) : null,
                chatRoom.getMembers().stream()
                        .map(ChatRoomMemberResponse::from)
                        .collect(Collectors.toList()),
                chatRoom.getCreatedAt(),
                unreadCount
        );
    }
}
