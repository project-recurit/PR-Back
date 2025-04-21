package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.List;

@Getter
public class PrListResponse {
    private final Pr pr;
    private final User user;
    private List<TechStackDto> techStacks;

    public PrListResponse(Long prId, String title, WorkType workType, int viewCount, int commentCount, int favoriteCount, String nickname, String profileUrl, Position position) {
        this.pr = new Pr(prId, title, workType, viewCount, commentCount, favoriteCount);
        this.user = new User(nickname, profileUrl, position);
    }

    public PrListResponse addTechStacks(List<TechStackDto> techStacks) {
        this.techStacks = techStacks;
        return this;
    }
}

record Pr(
        Long id,
        String title,
        WorkType workType,
        int viewCount,
        int commentCount,
        int favoriteCount
) {
}

record User(
        String nickname,
        String profileUrl,
        Position position
) {
}