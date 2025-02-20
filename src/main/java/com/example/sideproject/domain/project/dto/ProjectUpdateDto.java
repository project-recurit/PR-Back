package com.example.sideproject.domain.project.dto;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.project.entity.RecruitStatus;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProjectUpdateDto(
        String title,
        String content,
        String expectedPeriod,
        String contact,
        String recruitmentPeriod,
        String teamSize,
        String recruitStatus,
        List<ProjectTechStack> projectTechStacks,
        List<Long> existFiles,
        List<MultipartFile> newFiles
) {
    public Project update(User user, Long projectId) {
        return Project.builder()
                .id(projectId)
                .title(title)
                .contact(contact)
                .content(content)
                .expectedPeriod(expectedPeriod)
                .recruitmentPeriod(recruitmentPeriod)
                .recruitStatus(RecruitStatus.valueOf(recruitStatus))
                .user(user)
                .teamSize(teamSize)
                .build();
    }
}
