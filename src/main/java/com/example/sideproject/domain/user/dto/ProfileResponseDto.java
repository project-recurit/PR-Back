package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserTechStack;
import com.example.sideproject.global.enums.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProfileResponseDto {
    private String nickname;
    private String email;
    private String profileUrl;
    private String bio;
    private Position position;
    private List<UserTechStack> techStacks;

    @Builder
    public ProfileResponseDto(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileUrl = user.getProfileUrl();
        this.bio = user.getBio();
        this.position = user.getPosition();
        this.techStacks = new ArrayList<>();
    }
}
