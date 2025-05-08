package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.PrExperience;
import com.example.sideproject.domain.pr.entity.PrTechStack;
import com.example.sideproject.global.dto.PostResponseDto;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class PrResponse extends PostResponseDto {
    private final WorkType workType;
    private List<PrTechStackResponse> techStacks;
    private final Position position;
    private final String introduce;
    private List<PrExperienceResponse> experiences;

    @QueryProjection
    public PrResponse(Long id, String nickname, String profileUrl, Position position, String title, String introduce, WorkType workType,
                      int viewCount, int commentCount, int favoriteCount, String createdAt, String modifiedAt) {
        super(id, title, nickname, profileUrl, viewCount, commentCount, favoriteCount, createdAt, modifiedAt);
        this.position = position;
        this.introduce = introduce;
        this.workType = workType;
    }

    public PrResponse(Pr pr) {
        this(pr.getId(), pr.getUser().getNickname(), pr.getUser().getProfileUrl(), pr.getPosition(), pr.getTitle(), pr.getIntroduce(),
             pr.getWorkType(), pr.getCount().getViewCount(), pr.getCount().getCommentCount(), pr.getCount().getFavoriteCount(), pr.getCreatedAtToString(),
             pr.getModifiedAtToString());
        setTechStacks(pr.getTechStacks());
        setExperiences(pr.getExperiences());
    }

    public PrResponse setTechStacks(List<PrTechStack> techStacks) {
        this.techStacks = of(techStacks);
        return this;
    }

    private List<PrTechStackResponse> of(List<PrTechStack> techStacks) {
        return techStacks.stream()
                .map(t -> new PrTechStackResponse(
                        t.getTechStack().getId(),
                        t.getTechStack().getName(),
                        t.getLevel())
                ).toList();
    }

    private PrResponse setExperiences(List<PrExperience> experiences) {
        this.experiences = experiences.stream()
                .map(e -> PrExperienceResponse.builder()
                        .id(e.getId())
                        .title(e.getTitle())
                        .description(e.getDescription())
                        .teamSize(e.getTeamSize())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .documentUrl(e.getDocumentUrl())
                        .createdAt(e.getCreatedAt())
                        .modifiedAt(e.getModifiedAt())
                        .build()
                ).toList();
        return this;
    }

}

