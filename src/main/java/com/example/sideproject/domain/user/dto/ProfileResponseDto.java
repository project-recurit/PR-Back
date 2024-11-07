package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponseDto {
    private String nickname;
    private String email;
    private String contact;
    private String profileUrl;

    @Builder
    public ProfileResponseDto(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.contact = user.getContact();
        this.profileUrl = user.getProfileUrl();
    }
}
