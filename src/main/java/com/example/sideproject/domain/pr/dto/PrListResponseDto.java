package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.global.dto.PostResponseDto;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import lombok.Getter;

import java.util.List;

@Getter
public class PrListResponseDto extends PostResponseDto {
    private final WorkType workType;
    private final Position position;
    private List<PrTechStackResponse> techStacks;

    public PrListResponseDto(Long id, String title, String nickname, String profileUrl, int viewCount, int commentCount, int favoriteCount, String createdAt, String modifiedAt, WorkType workType, Position position) {
        super(id, title, nickname, profileUrl, viewCount, commentCount, favoriteCount, createdAt, modifiedAt);
        this.workType = workType;
        this.position = position;
    }

    public PrListResponseDto addTechStacks(List<PrTechStackResponse> techStacks) {
        this.techStacks = techStacks;
        return this;
    }

}
