package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.techstack.TechStack;
import com.example.sideproject.domain.user.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public record ResumeRequestDto(
        String position,
        String title,
        String introduce,
        List<String> documentUrl,
        List<Long> techStackIds,
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
                .documentUrl(documentUrl)
                .resumeTechStacks(techStackIds.stream().map(id -> TechStack.builder().id(id).build()).toList())
                .experiences(experiences.stream().map(ExperienceRequestDto::toEntity).toList())
                .build();
    }
}
