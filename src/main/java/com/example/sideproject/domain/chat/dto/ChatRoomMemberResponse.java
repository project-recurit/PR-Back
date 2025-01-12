package com.example.sideproject.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomMemberResponse {
    private Long userId;
    private String nickname;
    private LocalDateTime lastReadAt;
    private boolean isLeft;
    private LocalDateTime leftAt;
}
