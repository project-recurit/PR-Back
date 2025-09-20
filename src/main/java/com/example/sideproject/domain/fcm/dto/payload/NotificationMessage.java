package com.example.sideproject.domain.fcm.dto.payload;

// 메시지 브로커로 전송할 객체
public record NotificationMessage(
        Long userId,
        String title,
        String body
) {
}
