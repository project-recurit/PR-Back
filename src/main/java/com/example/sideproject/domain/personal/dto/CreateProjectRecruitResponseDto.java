package com.example.sideproject.domain.personal.dto;

import java.util.Set;

import com.example.sideproject.domain.personal.entity.ProjectRecruit;
import com.example.sideproject.domain.user.entity.TechStack;

import lombok.Getter;

@Getter
public class CreateProjectRecruitResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final Set<TechStack> techStacks;
    private final String expectedPeriod;
    private final String fileUrl;
    private final String contact;

    public CreateProjectRecruitResponseDto(ProjectRecruit projectRecruit) {
        this.id = projectRecruit.getId();
        this.title = projectRecruit.getTitle();
        this.content = projectRecruit.getContent();
        this.techStacks = projectRecruit.getTechStacks();
        this.expectedPeriod = projectRecruit.getExpectedPeriod();
        this.fileUrl = projectRecruit.getFileUrl();
        this.contact = projectRecruit.getContact();
    }
} 