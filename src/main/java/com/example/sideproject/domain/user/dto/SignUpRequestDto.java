package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.TechStack;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor  // 기본 생성자
public class SignUpRequestDto {

    @Size(min = 4, max = 20)
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{}|;:'\",.<>?/]+$",
            message = "비밀번호는 알파벳 대소문자, 숫자 및 특수문자만 포함할 수 있습니다.")
    @Size(min = 8, max = 30)
    private String password;

    private String checkPassword;

    @Email
    private String email;

    private String nickname;

    private String contact;

    private Set<TechStack> techStacks;

    private String socialId;

    private String socialProvider;

    @Builder
    public SignUpRequestDto(String username, String password, String checkPassword,
                            String email, String nickname, String contact,
                            Set<TechStack> techStacks, String socialId, String socialProvider) {
        this.username = username;
        this.password = password;
        this.checkPassword = checkPassword;
        this.email = email;
        this.nickname = nickname;
        this.contact = contact;
        this.techStacks = techStacks;
        this.socialId = socialId;
        this.socialProvider = socialProvider;
    }
}