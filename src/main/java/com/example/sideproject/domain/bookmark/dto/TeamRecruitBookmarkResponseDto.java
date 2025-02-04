package com.example.sideproject.domain.bookmark.dto;


import java.util.Set;

import com.example.sideproject.domain.user.entity.TechStack1;
import lombok.Getter;
import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;
import com.example.sideproject.domain.project.entity.Project;

@Getter
public class TeamRecruitBookmarkResponseDto {
    // 북마크 정보
    private final Long bookmarkId;
    private final String createdAt;
    private final String modifiedAt;
    
    // 북마크한 사용자 정보
    private final Long userId;
    private final String username;
    
    // 팀 모집글 정보
    private final Long teamRecruitId;
    private final String title;
    private final String content;
//    private final Set<TechStack1> techStack1s;
    private final String expectedPeriod;
    private final String contact;

    public TeamRecruitBookmarkResponseDto(TeamRecruitBookmark bookmark) {
        // 북마크 정보 매핑
        this.bookmarkId = bookmark.getId();
        this.createdAt = bookmark.getCreatedAt();
        this.modifiedAt = bookmark.getModifiedAt();
        
        // 사용자 정보 매핑
        this.userId = bookmark.getUser().getId();
        this.username = bookmark.getUser().getUsername();
        
        // 팀 모집글 정보 매핑
        Project recruit = bookmark.getProject();
        this.teamRecruitId = recruit.getId();
        this.title = recruit.getTitle();
        this.content = recruit.getContent();
//        this.techStack1s = recruit.getTechStack1s();
        this.expectedPeriod = recruit.getExpectedPeriod();
        this.contact = recruit.getContact();
    }
} 