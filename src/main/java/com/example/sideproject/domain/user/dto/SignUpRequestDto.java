package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserTechStack;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SignUpRequestDto(
        String username,
        @Email
        String email,
        String nickname,
        String socialId,
        String socialProvider,
        String position,
        List<Long> techStackIds
) {
    // Entity 변환 메서드
    public User toEntity() {
        return User.builder()
                .username(username)
                .email(email)
                .nickname(nickname)
                .userTechStacks(techStackIds.stream().map(id -> TechStack.builder().id(id).build()).toList())
                .socialId(socialId)
                .socialProvider(socialProvider)
                .position(position)
                .build();
    }
}