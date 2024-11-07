package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.User;
import lombok.Getter;

@Getter
public class SignUpResponseDto {
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String phone;

    public SignUpResponseDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.phone = user.getContact();
    }
}
