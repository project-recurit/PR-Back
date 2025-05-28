package com.example.sideproject.domain.fcm.dto.payload;

public record NotificationMessage(
        Long userId,
        String title,
        String body
) {
}
