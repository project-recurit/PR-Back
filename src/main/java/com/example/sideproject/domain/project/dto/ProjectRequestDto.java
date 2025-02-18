package com.example.sideproject.domain.project.dto;


import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.RecruitStatus;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public record ProjectRequestDto(

     String title,
     String content,
     String expectedPeriod,
     String recruitmentPeriod,
     String teamSize,
     List<Long> projectTechStacks,
     List<MultipartFile> files
) {
    public Project toEntity(User user) {
        return Project.builder()
                .title(title)
                .content(content)
                .expectedPeriod(expectedPeriod)
                .recruitmentPeriod(recruitmentPeriod)
                .user(user)
                .teamSize(teamSize)
                .viewCount(0)
                .likeCount(0)
                .recruitStatus(RecruitStatus.IN_PROGRESS)
                .build();
    }
}
