package com.example.sideproject.domain.project.dto;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.RecruitStatus;
import com.example.sideproject.domain.user.entity.User;

import java.util.List;


public record CreateTeamRecruitRequestDto (

     String title,
     String content,
     String expectedPeriod,
     String contact,
     String recruitmentPeriod,
     List<Long> projectTechStacks
) {
    public Project toEntity(User user) {
        return Project.builder()
                .title(title)
                .contact(contact)
                .content(content)
                .expectedPeriod(expectedPeriod)
                .recruitmentPeriod(recruitmentPeriod)
                .user(user)
                .viewCount(0)
                .likeCount(0)
                .recruitStatus(RecruitStatus.IN_PROGRESS)
                .build();
    }
}
