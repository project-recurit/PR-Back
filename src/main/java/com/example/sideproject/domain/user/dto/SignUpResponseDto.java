package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.User;
import lombok.Getter;

@Getter
public class SignUpResponseDto {
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String name;
    private String phone;
    private String headline;

    public SignUpResponseDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.name = user.getName();
        this.headline = user.getHeadline();
        this.phone = user.getPhoneNumber();
    }
}
