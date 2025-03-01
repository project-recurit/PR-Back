package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.chat.entity.ChatRoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequest {
    private Long senderId;
    private Long receiverId;
    private ChatRoomType chatRoomType;
    private Long referenceId;
}