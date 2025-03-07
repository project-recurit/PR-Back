package com.example.sideproject.domain.project.dto;

import com.example.sideproject.domain.project.entity.*;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.WorkType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProjectUpdateDto(
        String title,
        String content,
        EstimatedDuration estimatedDuration, // 기존 expectedPeriod → estimatedDuration 변경 (Enum 적용)
        String contact,
        String deadLine, // 기존 recruitmentPeriod → deadLine 변경
        int recruitmentCapacity, // 기존 teamSize → recruitmentCapacity 변경
        String isRecruiting, // 기존 recruitStatus → boolean 값 변경
        WorkType workType,
        List<Long> projectTechStacks,
        List<Long> existFiles,
        List<MultipartFile> newFiles
) {
    public Project update(User user, Long projectId, List<ProjectTechStack> projectTechStacks, List<ProjectUrl> projectUrls) {

        boolean recruiting;
        if(isRecruiting == "TRUE") {
            recruiting = true;
        } else {
            recruiting = false;
        }

        return Project.builder()
                .id(projectId)
                .title(title)
                .content(content)
                .estimatedDuration(estimatedDuration)
                .deadLine(deadLine)
                .isRecruiting(recruiting) // 모집 상태 (true: 모집 중, false: 모집 종료)
                .user(user)
                .recruitmentCapacity(recruitmentCapacity)
                .projectTechStacks(projectTechStacks)
                .projectUrls(projectUrls)
                .workType(workType)
                .build();
    }
}
