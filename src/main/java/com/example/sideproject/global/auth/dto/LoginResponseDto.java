package com.example.sideproject.global.auth.dto;

import com.example.sideproject.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record LoginResponseDto(
        String loginId,
        String accessToken,
        String refreshToken,
        boolean isFirstLogin
) {
    // 최초 로그인(회원가입) 응답을 위한 팩토리 메서드
    public static LoginResponseDto ofSignUp(String loginId) {
        return new LoginResponseDto(
                loginId,
                null,
                null,
                true   // isFirstLogin
        );
    }

    // 일반 로그인 성공 응답을 위한 팩토리 메서드
    public static LoginResponseDto ofLogin(
            String loginId,
            String accessToken,
            String refreshToken
    ) {
        return new LoginResponseDto(
                loginId,
                accessToken,
                refreshToken,
                false   // isFirstLogin
        );
    }
}
