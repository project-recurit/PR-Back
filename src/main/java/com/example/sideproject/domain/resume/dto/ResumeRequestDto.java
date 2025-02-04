package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.user.entity.User;

import java.util.List;

public record ResumeRequestDto(
        String title,
        String introduce,
        List<String> documentUrl,
        List<ExperienceRequestDto> experiences
) {
    public Resume toEntity(User user) {
        return Resume.builder()
                .user(user)
                .title(title)
                .introduce(introduce)
                .documentUrl(documentUrl)
                .experiences(experiences.stream().map(ExperienceRequestDto::toEntity).toList())
                .build();
    }
}
