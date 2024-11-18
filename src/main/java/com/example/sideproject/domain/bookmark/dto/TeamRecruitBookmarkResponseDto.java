package com.example.sideproject.domain.bookmark.dto;


import java.util.Set;

import lombok.Getter;
import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;
import com.example.sideproject.domain.user.entity.TechStack;
import com.example.sideproject.domain.team.entity.TeamRecruit;

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
    private final Set<TechStack> techStacks;
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
        TeamRecruit recruit = bookmark.getTeamRecruit();
        this.teamRecruitId = recruit.getId();
        this.title = recruit.getTitle();
        this.content = recruit.getContent();
        this.techStacks = recruit.getTechStacks();
        this.expectedPeriod = recruit.getExpectedPeriod();
        this.contact = recruit.getContact();
    }
} 