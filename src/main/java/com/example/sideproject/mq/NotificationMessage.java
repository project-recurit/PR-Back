package com.example.sideproject.mq;

public record NotificationMessage(
        Long userId,
        String title,
        String body
) {
}
