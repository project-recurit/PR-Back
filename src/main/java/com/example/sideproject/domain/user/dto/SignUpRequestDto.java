package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.TechStack;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Set;

@Getter
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

    private String phone;

    private Set<TechStack> techStacks;



}
