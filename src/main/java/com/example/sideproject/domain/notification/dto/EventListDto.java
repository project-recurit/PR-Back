package com.example.sideproject.domain.notification.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record EventListDto(
        List<EventDto> eventDtos
) {
}
