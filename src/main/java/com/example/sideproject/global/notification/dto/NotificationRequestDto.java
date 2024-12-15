package com.example.sideproject.global.notification.dto;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.notification.entity.Notification;
import com.example.sideproject.global.notification.entity.NotificationType;

public record NotificationRequestDto(
        Long to,
        NotificationType type,
        String message,
        String relatedUrl
) {
    public Notification toEntity(User from) {
        return Notification.builder()
                .to(new User(to))
                .from(from)
                .type(type)
                .message(message)
                .relatedUrl(relatedUrl)
                .build();
    }
}
