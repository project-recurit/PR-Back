package com.example.sideproject.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequest {
    private Long senderId;
    private Long receiverId;
}