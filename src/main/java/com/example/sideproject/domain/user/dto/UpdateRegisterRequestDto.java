package com.example.sideproject.domain.user.dto;


import java.util.List;

public record UpdateRegisterRequestDto(
        String socialId,
        String position,
        List<Long> techStackIds,
        String nickname
) {
}
