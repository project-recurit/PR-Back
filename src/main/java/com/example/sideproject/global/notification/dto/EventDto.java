package com.example.sideproject.global.notification.dto;

import com.example.sideproject.global.notification.entity.NotificationType;
import lombok.Builder;

@Builder
public record EventDto(
        Long to,
        Long from,
        String msg,
        NotificationType type,
        String relatedUrl
) {

}
