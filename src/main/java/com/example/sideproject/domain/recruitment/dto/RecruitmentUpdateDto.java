package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.domain.recruitment.entity.*;
import com.example.sideproject.domain.recruitment.entity.EstimatedDuration;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentTechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.WorkType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record RecruitmentUpdateDto(
        String title,
        String content,
        EstimatedDuration estimatedDuration, // 기존 expectedPeriod → estimatedDuration 변경 (Enum 적용)
        String contact,
        String deadLine, // 기존 recruitmentPeriod → deadLine 변경
        int recruitmentCapacity, // 기존 teamSize → recruitmentCapacity 변경
        String isRecruiting, // 기존 recruitStatus → boolean 값 변경
        WorkType workType,
        List<Long> recruitmentTechStacks,
        List<Long> existFiles,
        List<MultipartFile> newFiles
) {
    public Recruitment update(User user, Long recruitmentId, List<RecruitmentTechStack> recruitmentTechStacks, List<RecruitmentImage> recruitmentImages) {

        boolean recruiting;
        if(isRecruiting == "TRUE") {
            recruiting = true;
        } else {
            recruiting = false;
        }

        return Recruitment.builder()
                .id(recruitmentId)
                .title(title)
                .content(content)
                .estimatedDuration(estimatedDuration)
                .deadLine(deadLine)
                .isRecruiting(recruiting) // 모집 상태 (true: 모집 중, false: 모집 종료)
                .user(user)
                .recruitmentCapacity(recruitmentCapacity)
                .recruitmentTechStacks(recruitmentTechStacks)
                .recruitmentImages(recruitmentImages)
                .workType(workType)
                .build();
    }
}
