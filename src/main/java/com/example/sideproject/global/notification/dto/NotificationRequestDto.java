package com.example.sideproject.global.notification.dto;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.notification.entity.Notification;
import com.example.sideproject.global.notification.entity.NotificationType;
import lombok.Builder;

@Builder
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

    public static NotificationRequestDto of(EventDto eventDto) {
        return NotificationRequestDto.builder()
                .to(eventDto.to())
                .type(eventDto.type())
                .message(eventDto.msg())
                .relatedUrl(eventDto.relatedUrl())
                .build();
    }
}
