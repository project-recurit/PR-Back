package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.chat.entity.ChatMessage;
import com.example.sideproject.domain.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        ProjectSummaryResponse project // 프로젝트 정보 추가
) {
    public static ChatRoomDetailResponse of(
            ChatRoom chatRoom,
            List<ChatMessageResponse> messageResponses,
            Page<ChatMessage> messagePage
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
                ProjectSummaryResponse.from(chatRoom.getProject())
        );
    }
}