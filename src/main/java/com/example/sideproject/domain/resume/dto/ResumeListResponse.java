package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.techstack.dto.TechStackMapping;
import com.example.sideproject.domain.techstack.dto.TechStackResponse;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.dto.TechStackVo;
import com.example.sideproject.global.enums.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class ResumeListResponse implements TechStackResponse {
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

    public ResumeListResponse(Long resumeId, String title, Position position, WorkType workType, LocalDateTime modifiedAt, LocalDateTime createdAt) {
        this.id = resumeId;
        this.title = title;
        this.position = position;
        this.workType = workType;
        this.modifiedAt = modifiedAt.toString();
        this.createdAt = createdAt.toString();
    }

    @Override
    public TechStackResponse setTechStacks(List<TechStackMapping> techStacks) {
        this.techStacks = TechStackDto.from(techStacks);
        return this;
    }

    @Override
    public Long getTargetId() {
        return id;
    }
}
