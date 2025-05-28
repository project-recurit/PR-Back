package com.example.sideproject.domain.notification.dto;

import com.example.sideproject.domain.notification.entity.Notification;
import lombok.Builder;

@Builder
public record NotificationDto(
        Long notificationId,
        Long userId,
        String type,
        String message,
        Long relatedId,
        boolean isRead
) {
    public static NotificationDto of(Notification notification) {
        return NotificationDto.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getTo().getId())
                .type(notification.getType().name())
                .message(notification.getMessage())
                .relatedId(notification.getRelatedId())
                .isRead(notification.isRead())
                .build();
    }
}
