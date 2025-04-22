package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.PrExperience;
import com.example.sideproject.domain.pr.entity.PrTechStack;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class PrResponse {
    private final Long id;
    private final UserResponse user;
    private final WorkType workType;
    private List<PrTechStackResponse> techStacks;
    private final Position position;
    private final String title;
    private final String introduce;
    private List<PrExperienceResponse> experiences;

    @QueryProjection
    public PrResponse(Long id,
                      String nickname,
                      String profileUrl,
                      Position position,
                      String title,
                      String introduce,
                      WorkType workType) {
        this.id = id;
        this.user = new UserResponse(nickname, profileUrl);
        this.position = position;
        this.title = title;
        this.introduce = introduce;
        this.workType = workType;
    }

    public PrResponse(Pr pr) {
        this.id = pr.getId();
        this.title = pr.getTitle();
        this.introduce = pr.getIntroduce();
        this.position = pr.getPosition();
        this.workType = pr.getWorkType();
        setTechStacks(pr.getTechStacks());
        setExperiences(pr.getExperiences());
        this.user = new UserResponse(pr.getUser().getNickname(), pr.getUser().getProfileUrl());
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
                        .build()
                ).toList();
        return this;
    }

    record UserResponse(
            String nickname,
            String profileUrl
    ) {}
}

