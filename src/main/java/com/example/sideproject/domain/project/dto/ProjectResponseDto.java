package com.example.sideproject.domain.project.dto;

import com.example.sideproject.domain.project.entity.ProjectMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProjectResponseDto {
    private final String title;           // 제목
    private final String description;     // 내용
    private final String projectUrl;      // 프로젝트 URL (nullable)
    private final int teamCount;          // 참여인원
    private final LocalDateTime startDate; // 시작 날짜
    private final LocalDateTime endDate;   // 종료 날짜 (nullable)
    private final String nickname;
    private final List<ProjectMember> projectMemberList;

    @Builder
    public ProjectResponseDto(String title, String description, String projectUrl, int teamCount, LocalDateTime startDate, LocalDateTime endDate, String nickname, List<ProjectMember> projectMemberList) {
        this.title = title;
        this.description = description;
        this.projectUrl = projectUrl;
        this.teamCount = teamCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nickname = nickname;
        this.projectMemberList = projectMemberList;
    }
}
