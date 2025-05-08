package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.domain.recruitment.entity.*;
import com.example.sideproject.domain.recruitment.entity.EstimatedDuration;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentTechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.WorkType;

import java.util.List;

public record RecruitmentUpdateDto(
        String title,
        String content,
        EstimatedDuration estimatedDuration, // 기존 expectedPeriod → estimatedDuration 변경 (Enum 적용)
        String contact,
        String deadLine, // 기존 recruitmentPeriod → deadLine 변경
        int recruitmentCapacity, // 기존 teamSize → recruitmentCapacity 변경
        boolean isRecruiting, // 기존 recruitStatus → boolean 값 변경
        WorkType workType,
        RecruitmentCategory recruitmentCategory,
        boolean isCommercial,
        List<Long> techStackIds,
        List<Long> existFiles
) {
    public Recruitment update(User user, Long recruitmentId, List<RecruitmentTechStack> recruitmentTechStacks,
                              List<RecruitmentImage> recruitmentImages, List<RecruitmentPosition> positions) {

        return Recruitment.builder()
                .id(recruitmentId)
                .title(title)
                .content(content)
                .estimatedDuration(estimatedDuration)
                .deadLine(deadLine)
                .isRecruiting(isRecruiting) // 모집 상태 (true: 모집 중, false: 모집 종료)
                .user(user)
                .recruitmentTechStacks(recruitmentTechStacks)
                .recruitmentImages(recruitmentImages)
                .positions(positions)
                .workType(workType)
                .isCommercial(isCommercial)
                .recruitmentCategory(recruitmentCategory)
                .build();
    }
}
