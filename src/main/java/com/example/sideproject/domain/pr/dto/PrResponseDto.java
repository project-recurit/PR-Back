package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.resume.dto.TechStackDto;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.global.enums.WorkType;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PrResponseDto {
    @Schema(description = "prId == resumeId")
    private Long prId;
    private String nickname;
    private String position;
    private String title;
    private String introduce;
    WorkType workType;
    List<String> techStacks;
    LocalDateTime publishedAt;

    @QueryProjection
    public PrResponseDto(Long resumeId, String nickname, String position, String title, String introduce, WorkType workType, LocalDateTime publishedAt) {
        this.prId = resumeId;
        this.nickname = nickname;
        this.position = position;
        this.title = title;
        this.introduce = introduce;
        this.workType = workType;
        this.publishedAt = publishedAt;
    }

    public PrResponseDto setTechStacks(List<String> techStacks) {
        this.techStacks = techStacks;
        return this;
    }
}
