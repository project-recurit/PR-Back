package com.example.sideproject.domain.team.dto;

import com.example.sideproject.domain.team.entity.TeamRecruit;
import com.example.sideproject.domain.user.entity.TechStack;
import lombok.Getter;
import java.util.Set;

@Getter
public class CreateTeamRecruitResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final Set<TechStack> techStacks;
    private final String expectedPeriod;
    private final String fileUrl;
    private final String contact;

    public CreateTeamRecruitResponseDto(TeamRecruit teamRecruit) {
        this.id = teamRecruit.getId();
        this.title = teamRecruit.getTitle();
        this.content = teamRecruit.getContent();
        this.techStacks = teamRecruit.getTechStacks();
        this.expectedPeriod = teamRecruit.getExpectedPeriod();
        this.fileUrl = teamRecruit.getFileUrl();
        this.contact = teamRecruit.getContact();
    }
}
