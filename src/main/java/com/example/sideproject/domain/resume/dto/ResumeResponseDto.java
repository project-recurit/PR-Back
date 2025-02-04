package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Resume;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ResumeResponseDto(
        Long resumeId,
        Long userId,
        String title,
        String introduce,
        List<String> documentUrl,
        List<ExperienceResponseDto> experiences,
        LocalDateTime publishedAt,
        String createdAt,
        String modifiedAt
) {
    public static ResumeResponseDto of(Resume resume) {
        return ResumeResponseDto.builder()
                .resumeId(resume.getId())
                .userId(resume.getUser().getId())
                .title(resume.getTitle())
                .introduce(resume.getIntroduce())
                .documentUrl(resume.getDocumentUrl())
                .experiences(resume.getExperiences().stream().map(ExperienceResponseDto::of).toList())
                .publishedAt(resume.getPublishedAt())
                .createdAt(resume.getCreatedAt())
                .modifiedAt(resume.getModifiedAt())
                .build();
    }
}
