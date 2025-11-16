package com.example.sideproject.global.auth.dto;

import com.example.sideproject.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record LoginResponseDto(
        String id,
        String accessToken,
        String refreshToken,
        boolean isSignUpSuccess,
        Long userId
) {
    // 최초 로그인(회원가입) 응답을 위한 팩토리 메서드
    public static LoginResponseDto ofSignUp(String id, Long userId) {
        return new LoginResponseDto(
                id,
                null,
                null,
                true,// isSignUpSuccess
                userId
        );
    }

    // 일반 로그인 성공 응답을 위한 팩토리 메서드
    public static LoginResponseDto ofLogin(
            String id,
            String accessToken,
            String refreshToken,
            Long userId
    ) {
        return new LoginResponseDto(
                id,
                accessToken,
                refreshToken,
                false,// isSignUpSuccess
                userId
        );
    }
}
