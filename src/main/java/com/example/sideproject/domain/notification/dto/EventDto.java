package com.example.sideproject.domain.notification.dto;

import com.example.sideproject.domain.notification.entity.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public record EventDto(
        Long to,
        // 본인에게 전송하지 않기 위함
        @JsonIgnore
        Long from,
        String title,
        String msg,
        NotificationType type,
        Long relatedId,
        boolean pushAllowed,
        boolean needToPush
) {
}
