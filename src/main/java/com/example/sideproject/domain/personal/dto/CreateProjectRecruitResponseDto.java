package com.example.sideproject.domain.personal.dto;

import java.util.Set;

import com.example.sideproject.domain.personal.entity.ProjectRecruit;
import com.example.sideproject.domain.user.entity.TechStack;

import lombok.Getter;

@Getter
public class CreateProjectRecruitResponseDto {
    private Long id;
    private String title;
    private String content;
    private Set<TechStack> techStacks;
    private String expectedPeriod;
    private String fileUrl;
    private String contact;

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