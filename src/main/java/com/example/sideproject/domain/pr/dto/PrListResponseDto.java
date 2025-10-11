package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.techstack.dto.TechStackMapping;
import com.example.sideproject.domain.techstack.dto.TechStackResponse;
import com.example.sideproject.global.dto.PostResponseDto;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
public class PrListResponseDto extends PostResponseDto implements TechStackResponse {
    private final WorkType workType;
    private final Position position;
    private List<PrTechStackResponse> techStacks;

    public PrListResponseDto(Long id, String title, String nickname, String profileUrl, int viewCount, int commentCount, int favoriteCount,
                             LocalDateTime createdAt, LocalDateTime modifiedAt, WorkType workType, Position position) {
        super(id, title, nickname, profileUrl, viewCount, commentCount, favoriteCount, createdAt, modifiedAt);
        this.workType = workType;
        this.position = position;
    }

    public PrListResponseDto addTechStacks(List<PrTechStackResponse> techStacks) {
        this.techStacks = Objects.requireNonNullElse(techStacks, List.of());
        return this;
    }

    @Override
    public TechStackResponse setTechStacks(List<TechStackMapping> techStacks) {
        this.techStacks = techStacks.stream()
                .map(t -> (PrTechStackResponse) t)
                .toList();
        return this;
    }

    @Override
    public Long getTargetId() {
        return getId();
    }
}
