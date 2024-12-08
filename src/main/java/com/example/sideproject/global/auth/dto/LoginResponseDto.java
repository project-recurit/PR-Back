package com.example.sideproject.global.auth.dto;

import com.example.sideproject.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String loginId;
    private UserRole userRole;
    private String accessToken;
    private String refreshToken;
}
