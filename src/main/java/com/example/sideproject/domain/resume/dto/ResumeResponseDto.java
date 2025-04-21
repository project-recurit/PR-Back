package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.global.enums.Position;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.global.enums.WorkType;
import lombok.Builder;

import java.util.List;

@Builder
public record ResumeResponseDto(
        Long id,
        Long userId,
        Position position,
        String title,
        String introduce,
        WorkType workType,
        List<String> documentUrl,
        List<TechStackDto> techStacks,
        List<ExperienceResponse> experiences,
        String createdAt,
        String modifiedAt
) {
    public static ResumeResponseDto of(Resume resume) {
        return ResumeResponseDto.builder()
                .id(resume.getId())
                .userId(resume.getUser().getId())
                .position(resume.getPosition())
                .title(resume.getTitle())
                .introduce(resume.getIntroduce())
                .workType(resume.getWorkType())
                .documentUrl(resume.getDocumentUrl())
                .techStacks(resume.getResumeTechStacks().stream().map(TechStackDto::of).toList())
                .experiences(resume.getExperiences().stream().map(ExperienceResponse::of).toList())
                .createdAt(resume.getCreatedAt())
                .modifiedAt(resume.getModifiedAt())
                .build();
    }
}
