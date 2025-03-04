package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import jakarta.validation.constraints.Email;


public record SignUpRequestDto(
        String username,
        @Email
        String email,
        String nickname,
        String socialId,
        String socialProvider
) {

    public User toEntity(UserStatus userStatus) {
        return User.builder()
                .username(username)
                .email(email)
                .nickname(nickname)
                .socialId(socialId)
                .socialProvider(socialProvider)
                .userStatus(userStatus)
                .build();
    }
}