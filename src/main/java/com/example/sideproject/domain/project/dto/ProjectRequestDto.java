package com.example.sideproject.domain.project.dto;


import com.example.sideproject.domain.project.entity.EstimatedDuration;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.RecruitStatus;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public record ProjectRequestDto(
        String title,
        String content,
        String deadLine,  // 기존 recruitmentPeriod → deadLine 으로 변경
        EstimatedDuration estimatedDuration,  // 기존 expectedPeriod → estimatedDuration 변경
        int recruitmentCapacity,  // 기존 teamSize → recruitmentCapacity 변경
        List<Long> projectTechStacks,
        List<MultipartFile> files
) {
    public Project toEntity(User user) {
        return Project.builder()
                .title(title)
                .content(content)
                .deadLine(deadLine)
                .estimatedDuration(estimatedDuration)
                .recruitmentCapacity(recruitmentCapacity)
                .isRecruiting(true) // 기본값으로 모집 중으로 설정
                .user(user)
                .viewCount(0)
                .commentCount(0)
                .favoriteCount(0)
                .build();
    }
}
