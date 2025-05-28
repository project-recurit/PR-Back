package com.example.sideproject.domain.fcm.dto;

public record DeviceDto(
        Long userId,
        String token
) {}