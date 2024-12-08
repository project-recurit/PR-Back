package com.example.sideproject.domain.team.dto;

import com.example.sideproject.domain.user.entity.TechStack;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateTeamRecruitRequestDto {

    private String title;
    private String content;
    private Set<TechStack> techStacks;
    private String expectedPeriod;
    private String fileUrl;
    private String contact;

    public CreateTeamRecruitRequestDto(String title, String content, 
            Set<TechStack> techStacks, String expectedPeriod, 
            String fileUrl, String contact) {
        this.title = title;
        this.content = content;
        this.techStacks = techStacks;
        this.expectedPeriod = expectedPeriod;
        this.fileUrl = fileUrl;
        this.contact = contact;
    }
}
