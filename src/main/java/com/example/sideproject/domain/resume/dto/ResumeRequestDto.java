package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;

public record ResumeRequestDto(
        @Schema(description = "직무")
        String position,
        @Schema(description = "이력서 제목")
        String title,
        @Schema(description = "자기 소개")
        String introduce,
        @Schema(description = "희망 진행 방식")
        WorkType workType,
        @Schema(description = "이력서 링크")
        List<String> documentUrl,
        @Schema(description = "기술 스택 ID 리스트")
        List<Long> techStackIds,
        @Schema(description = "프로젝트 리스트")
        @Valid
        List<ExperienceRequestDto> experiences
) {
    public ResumeRequestDto {
        // 중복 제거
        techStackIds = new HashSet<>(techStackIds).stream().toList();
    }

    public Resume toEntity(User user) {
        return Resume.builder()
                .user(user)
                .position(position)
                .title(title)
                .introduce(introduce)
                .workType(workType)
                .documentUrl(documentUrl)
                .resumeTechStacks(techStackIds.stream().map(id -> TechStack.builder().id(id).build()).toList())
                .experiences(experiences.stream().map(ExperienceRequestDto::toEntity).toList())
                .build();
    }
}
