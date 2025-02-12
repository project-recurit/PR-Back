package com.example.sideproject.global.auth.dto;

import com.example.sideproject.domain.user.entity.UserTechStack;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
public class LoginRequestDto {
    private String email;
    private String socialId;
    private String provider;
    private String nickname;
}
