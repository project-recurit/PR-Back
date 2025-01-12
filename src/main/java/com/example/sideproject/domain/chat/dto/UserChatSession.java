package com.example.sideproject.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserChatSession {
    private Long userId;
    private Long roomId;
}
