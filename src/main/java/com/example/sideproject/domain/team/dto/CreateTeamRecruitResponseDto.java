package com.example.sideproject.domain.team.dto;

import com.example.sideproject.domain.team.entity.TeamRecruit;
import com.example.sideproject.domain.user.entity.TechStack;
import lombok.Getter;
import java.util.Set;

@Getter
public class CreateTeamRecruitResponseDto {

    private Long id;
    private String title;
    private String content;
    private Set<TechStack> techStacks;
    private String expectedPeriod;
    private String fileUrl;
    private String contact;

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
