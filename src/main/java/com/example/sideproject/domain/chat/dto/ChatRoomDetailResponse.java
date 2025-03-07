package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.chat.entity.ChatMessage;
import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.chat.entity.ChatRoomType;
import com.example.sideproject.domain.chat.service.ChatService;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public record ChatRoomDetailResponse(
        Long roomId,
        List<ChatMessageResponse> messages,
        ChatMessageResponse lastMessage,
        List<ChatRoomMemberResponse> members,
        LocalDateTime createdAt,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean hasNext,
        ChatRoomType type,
        ContentSummaryResponse referenceInfo
) {
    @Builder
    public ChatRoomDetailResponse {}

    public static ChatRoomDetailResponse of(
            ChatRoom chatRoom,
            List<ChatMessageResponse> messageResponses,
            Page<ChatMessage> messagePage,
            ContentSummaryResponse referenceInfo
    ) {
        return new ChatRoomDetailResponse(
                chatRoom.getId(),
                messageResponses,
                chatRoom.getLastMessage() != null ?
                        ChatMessageResponse.from(chatRoom.getLastMessage()) : null,
                chatRoom.getMembers().stream()
                        .map(ChatRoomMemberResponse::from)
                        .collect(Collectors.toList()),
                chatRoom.getCreatedAt(),
                messagePage.getNumber(),
                messagePage.getTotalPages(),
                messagePage.getTotalElements(),
                messagePage.hasNext(),
                chatRoom.getType(),
                referenceInfo
        );
    }
}