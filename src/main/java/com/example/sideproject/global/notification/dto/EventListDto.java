package com.example.sideproject.global.notification.dto;

import com.example.sideproject.global.notification.entity.NotificationType;
import lombok.Builder;

import java.util.List;

@Builder
public record EventListDto(
        List<EventDto> eventDtos
) {
}
