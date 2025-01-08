package com.example.sideproject.global.notification.dto;

import com.example.sideproject.global.notification.entity.Notification;
import lombok.Builder;

@Builder
public record NotificationDto(
        Long notificationId,
        Long userId,
        String type,
        String message,
        String relatedUrl
) {
    public static NotificationDto of(Notification notification) {
        return NotificationDto.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getTo().getId())
                .type(notification.getType().name())
                .message(notification.getMessage())
                .relatedUrl(notification.getRelatedUrl())
                .build();
    }
}
