package com.example.sideproject.domain.notification.dto;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.notification.entity.Notification;
import com.example.sideproject.domain.notification.entity.NotificationType;
import lombok.Builder;

@Builder
public record NotificationRequestDto(
        Long to,
        NotificationType type,
        String message,
        Long relatedId
) {
    public Notification toEntity() {
        return Notification.builder()
                .to(new User(to))
                .type(type)
                .message(message)
                .relatedId(relatedId)
                .build();
    }

    public static NotificationRequestDto of(EventDto eventDto) {
        return NotificationRequestDto.builder()
                .to(eventDto.to())
                .type(eventDto.type())
                .message(eventDto.msg())
                .relatedId(eventDto.relatedId())
                .build();
    }
}
