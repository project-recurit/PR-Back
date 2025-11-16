package com.example.sideproject.domain.user.dto;


import com.example.sideproject.global.enums.Position;

import java.util.List;

public record UpdateRegisterRequestDto(
        String socialId,
        Position position,
        List<Long> techStackIds,
        String nickname,
        String bio
) {
}
