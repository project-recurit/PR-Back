package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.TechStack;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor  // 기본 생성자
public class SignUpRequestDto {

    @Size(min = 4, max = 20)
    private String username;

    private String password;

    @Email
    private String email;

    private String nickname;

    private Set<TechStack> techStacks;

    private String socialId;

    private String socialProvider;

    @Builder
    public SignUpRequestDto(String username, String password,String email, String nickname, Set<TechStack> techStacks,
                            String socialId, String socialProvider) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.techStacks = techStacks;
        this.socialId = socialId;
        this.socialProvider = socialProvider;
    }
}