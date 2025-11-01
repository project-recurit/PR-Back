package com.example.sideproject.domain.notification.dto;

import com.example.sideproject.domain.notification.entity.Notification;
import com.example.sideproject.domain.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationDto(
        @Schema(description = "알림 id")
        Long notificationId,
        @Schema(description = "유저 id")
        Long userId,
        @Schema(description = "알림 타입")
        NotificationType type,
        @Schema(description = "알림 메시지")
        String message,
        @Schema(description = "알림 연관된 id")
        Long relatedId,
        @Schema(description = "알림 읽음 여부")
        boolean isRead,
        @Schema(description = "알림 발생 시간")
        LocalDateTime createdAt
) {
    public static NotificationDto of(Notification notification) {
        return NotificationDto.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getTo().getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .relatedId(notification.getRelatedId())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
