package com.example.sideproject.domain.project.dto;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.user.entity.TechStack1;
import lombok.Getter;
import java.util.Set;

@Getter
public class CreateTeamRecruitResponseDto {

    private final Long id;
    private final String title;
    private final String content;
//    private final Set<TechStack1> techStack1s;
    private final String expectedPeriod;
    private final String contact;

    public CreateTeamRecruitResponseDto(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.content = project.getContent();
//        this.techStack1s = project.getTechStack1s();
        this.expectedPeriod = project.getExpectedPeriod();
        this.contact = project.getContact();
    }
}
