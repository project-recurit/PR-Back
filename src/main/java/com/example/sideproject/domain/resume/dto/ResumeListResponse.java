package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.global.enums.Position;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.dto.TechStackMappingDto;
import com.example.sideproject.global.enums.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ResumeListResponse {
    @Schema(description = "이력서 고유번호")
    Long id;
    @Schema(description = "이력서 제목")
    String title;
    @Schema(description = "직무")
    Position position;
    @Schema(description = "기술스택")
    List<TechStackDto> techStacks;
    @Schema(description = "진행방식")
    WorkType workType;
    @Schema(description = "수정일")
    String modifiedAt;
    @Schema(description = "생성일")
    String createdAt;

    public ResumeListResponse(Long resumeId, String title, Position position, WorkType workType, String modifiedAt, String createdAt) {
        this.id = resumeId;
        this.title = title;
        this.position = position;
        this.workType = workType;
        this.modifiedAt = modifiedAt;
        this.createdAt = createdAt;
    }

    public ResumeListResponse addTechStack(List<TechStackMappingDto> techStacks) {
        this.techStacks = techStacks.stream()
                .map(t -> new TechStackDto(t.techStackId(), t.name()))
                .toList();
        return this;
    }
}
