package com.example.sideproject.domain.notification.fcm.dto;

public record DeviceDto(
        Long userId,
        String token
) {}