package com.example.sideproject.domain.project.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProjectRequestDto {
    private String title;           // 제목
    private String description;     // 내용
    private String projectUrl;      // 프로젝트 URL (nullable)
    private int teamCount;          // 참여인원
    private LocalDateTime startDate; // 시작 날짜
    private LocalDateTime endDate;   // 종료 날짜 (nullable)
    private List<Long> memberList; // 멤버리스트 프론트에서 작성한 유저id도 넣게 해야될듯

    public ProjectRequestDto() {
    }

    public ProjectRequestDto(String title, String description, String projectUrl,
                             int teamCount, LocalDateTime startDate, LocalDateTime endDate, List<Long> memberList) {
        this.title = title;
        this.description = description;
        this.projectUrl = projectUrl;
        this.teamCount = teamCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memberList = memberList;
    }
}
