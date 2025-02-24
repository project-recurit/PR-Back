package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.dto.TechStackMappingDto;
import com.example.sideproject.global.enums.WorkType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class ResumeListResponseDto {
    Long resumeId;
    String title;
    String position;
    List<TechStackDto> techStacks;
    WorkType workType;
    boolean isPublished;
    String modifiedAt;

    public ResumeListResponseDto(Long resumeId, String title, String position, WorkType workType, LocalDateTime publishedAt, String modifiedAt) {
        this.resumeId = resumeId;
        this.title = title;
        this.position = position;
        this.workType = workType;
        this.isPublished = publishedAt != null;
        this.modifiedAt = modifiedAt;
    }

    public ResumeListResponseDto addTechStack(List<TechStackMappingDto> techStacks) {
        this.techStacks = techStacks.stream()
                .map(t -> new TechStackDto(t.techStackId(), t.name()))
                .toList();
        return this;
    }
}
