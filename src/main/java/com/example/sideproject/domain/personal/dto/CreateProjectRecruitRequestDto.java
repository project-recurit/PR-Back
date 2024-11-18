package com.example.sideproject.domain.personal.dto;

import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.sideproject.domain.user.entity.TechStack;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateProjectRecruitRequestDto {
    private String title;
    private String content;
    private Set<TechStack> techStacks;
    private String expectedPeriod;
    private String fileUrl;
    private String contact;

    public CreateProjectRecruitRequestDto(String title, String content, 
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
