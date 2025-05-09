package com.example.sideproject.domain.project.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProjectRequestDto {
    private String title;           // 제목
    private String description;     // 내용
    private String projectUrl;      // 프로젝트 URL (nullable)
    private int teamCount;          // 참여인원
    private LocalDateTime startDate; // 시작 날짜
    private LocalDateTime endDate;   // 종료 날짜 (nullable)

    public ProjectRequestDto() {}

    public ProjectRequestDto(String title, String description, String projectUrl, int teamCount, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.projectUrl = projectUrl;
        this.teamCount = teamCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
