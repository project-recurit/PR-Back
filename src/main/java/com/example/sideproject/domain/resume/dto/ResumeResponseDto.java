package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.global.enums.WorkType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ResumeResponseDto(
        Long resumeId,
        Long userId,
        String position,
        String title,
        String introduce,
        WorkType workType,
        List<String> documentUrl,
        List<TechStackDto> techStacks,
        List<ExperienceResponseDto> experiences,
        LocalDateTime publishedAt,
        String createdAt,
        String modifiedAt
) {
    public static ResumeResponseDto of(Resume resume) {
        return ResumeResponseDto.builder()
                .resumeId(resume.getId())
                .userId(resume.getUser().getId())
                .position(resume.getPosition())
                .title(resume.getTitle())
                .introduce(resume.getIntroduce())
                .workType(resume.getWorkType())
                .documentUrl(resume.getDocumentUrl())
                .techStacks(resume.getResumeTechStacks().stream().map(TechStackDto::of).toList())
                .experiences(resume.getExperiences().stream().map(ExperienceResponseDto::of).toList())
                .publishedAt(resume.getPublishedAt())
                .createdAt(resume.getCreatedAt())
                .modifiedAt(resume.getModifiedAt())
                .build();
    }
}
