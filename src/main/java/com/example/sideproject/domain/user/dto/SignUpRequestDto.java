package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.UserTechStack;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor  // 기본 생성자
public class SignUpRequestDto {

    @Size(min = 4, max = 20)
    private String username;

    private String password;

    @Email
    private String email;

    private String nickname;

    private List<UserTechStack> userTechStacks;

    private String socialId;

    private String socialProvider;

    private String position;

    @Builder
    public SignUpRequestDto(String username, String password,String email, String nickname, List<UserTechStack> userTechStacks,
                            String socialId, String socialProvider, String position) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.userTechStacks = userTechStacks;
        this.socialId = socialId;
        this.socialProvider = socialProvider;
        this.position = position;
    }
}