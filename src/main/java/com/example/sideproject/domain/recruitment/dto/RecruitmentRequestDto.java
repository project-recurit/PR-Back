package com.example.sideproject.domain.recruitment.dto;


import com.example.sideproject.domain.recruitment.entity.EstimatedDuration;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.WorkType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public record RecruitmentRequestDto(
        String title,
        String content,
        String deadLine,  // 기존 recruitmentPeriod → deadLine 으로 변경
        EstimatedDuration estimatedDuration,  // 기존 expectedPeriod → estimatedDuration 변경
        int recruitmentCapacity,  // 기존 teamSize → recruitmentCapacity 변경
        WorkType workType,
        List<Long> recruitmentTechStacks,
        List<MultipartFile> files
) {
    public Recruitment toEntity(User user) {
        return Recruitment.builder()
                .title(title)
                .content(content)
                .deadLine(deadLine)
                .estimatedDuration(estimatedDuration)
                .recruitmentCapacity(recruitmentCapacity)
                .workType(workType)
                .isRecruiting(true) // 기본값으로 모집 중으로 설정
                .user(user)
                .viewCount(0)
                .commentCount(0)
                .favoriteCount(0)
                .build();
    }
}
