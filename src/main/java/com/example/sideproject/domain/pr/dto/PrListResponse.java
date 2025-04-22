package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import lombok.Getter;

import java.util.List;

@Getter
public class PrListResponse {
    private final Long id;
    private final String title;
    private final WorkType workType;
    private final int viewCount;
    private final int commentCount;
    private final int favoriteCount;
    private final User user;
    private List<PrTechStackResponse> techStacks;
    private final String createdAt;
    private final String modifiedAt;

    public PrListResponse(Long id,
                          String title,
                          WorkType workType,
                          Position position,
                          int viewCount,
                          int commentCount,
                          int favoriteCount,
                          String nickname,
                          String profileUrl,
                          String createdAt,
                          String modifiedAt) {
        this.id = id;
        this.title = title;
        this.workType = workType;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
        this.user = new User(nickname, profileUrl, position);
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public PrListResponse addTechStacks(List<PrTechStackResponse> techStacks) {
        this.techStacks = techStacks;
        return this;
    }

    record User(
            String nickname,
            String profileUrl,
            Position position
    ) {
    }

}
