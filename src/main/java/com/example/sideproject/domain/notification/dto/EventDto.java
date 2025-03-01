package com.example.sideproject.domain.notification.dto;

import com.example.sideproject.domain.notification.entity.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public record EventDto(
        Long to,
        @JsonIgnore
        Long from,
        String msg,
        NotificationType type,
        Long relatedId
) {
}
