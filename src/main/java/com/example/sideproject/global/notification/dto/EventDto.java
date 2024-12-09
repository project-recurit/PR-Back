package com.example.sideproject.global.notification.dto;

import lombok.Builder;

@Builder
public record EventDto(
        Long to,
        String msg,
        String type,
        String relatedUrl
) {

}
