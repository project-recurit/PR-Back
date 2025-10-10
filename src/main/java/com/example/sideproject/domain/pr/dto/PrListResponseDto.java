package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.global.dto.PostResponseDto;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
public class PrListResponseDto extends PostResponseDto {
    private final WorkType workType;
    private final Position position;
    private List<PrTechStackResponse> techStacks;

    public PrListResponseDto(Long id, String title, String nickname, String profileUrl, int viewCount, int commentCount, int favoriteCount,
                             LocalDateTime createdAt, LocalDateTime modifiedAt, WorkType workType, Position position) {
        super(id, title, nickname, profileUrl, viewCount, commentCount, favoriteCount, createdAt.toString(), modifiedAt.toString());
        this.workType = workType;
        this.position = position;
    }

    public PrListResponseDto addTechStacks(List<PrTechStackResponse> techStacks) {
        this.techStacks = Objects.requireNonNullElse(techStacks, List.of());
        return this;
    }

}
