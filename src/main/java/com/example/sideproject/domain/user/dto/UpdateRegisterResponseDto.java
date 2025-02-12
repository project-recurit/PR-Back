package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.User;

import java.util.List;

public record UpdateRegisterResponseDto(
        String nickname,
        String position,
        List<String> techStacks,
        String accessToken,
        String refreshToken
) {
    public static UpdateRegisterResponseDto ofUpdateRegister(
            User user,
            String accessToken,
            String refreshToken
    ) {
        return new UpdateRegisterResponseDto(
                user.getNickname(),
                user.getPosition(),
                user.getUserTechStacks().stream()
                        .map(uts -> uts.getTechStack().getName())
                        .toList(),
                accessToken,
                refreshToken
        );

    }
}
