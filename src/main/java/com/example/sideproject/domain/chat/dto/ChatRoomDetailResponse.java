package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.chat.entity.ChatMessage;
import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.chat.entity.ChatRoomType;
import com.example.sideproject.domain.pr.entity.PublicResumes;
import com.example.sideproject.domain.pr.repository.PublicResumesRepository;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.repository.ProjectRepository;
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
        Object referenceInfo  // ProjectSummaryResponse 또는 PRSummaryResponse
) {
    @Builder
    public ChatRoomDetailResponse {}

    public static ChatRoomDetailResponse of(
            ChatRoom chatRoom,
            List<ChatMessageResponse> messageResponses,
            Page<ChatMessage> messagePage,
            ProjectRepository projectRepository,
            PublicResumesRepository publicResumesRepository
    ) {
        Object referenceInfo = null;

        // 채팅방 타입에 따라 적절한 요약 정보 생성
        if (chatRoom.getType() == ChatRoomType.PROJECT) {
            Project project = projectRepository.findById(chatRoom.getReferenceId())
                    .orElse(null);
            if (project != null) {
                referenceInfo = ProjectSummaryResponse.from(project);
            }
        } else if (chatRoom.getType() == ChatRoomType.PR) {
            PublicResumes pr = publicResumesRepository.findById(chatRoom.getReferenceId())
                    .orElse(null);
            if (pr != null) {
                referenceInfo = PRSummaryResponse.from(pr);
            }
        }

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