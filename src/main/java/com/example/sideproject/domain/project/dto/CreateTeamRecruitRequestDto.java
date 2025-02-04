package com.example.sideproject.domain.project.dto;

import com.example.sideproject.domain.pr.entity.PublicRelationDetail;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.RecruitStatus;
import com.example.sideproject.domain.user.entity.TechStack1;

import com.example.sideproject.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.Set;


public record CreateTeamRecruitRequestDto (

     String title,
     String content,
     String expectedPeriod,
     String contact,
     String recruitmentPeriod
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
