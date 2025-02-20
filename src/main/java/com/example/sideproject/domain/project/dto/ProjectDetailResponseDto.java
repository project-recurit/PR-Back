package com.example.sideproject.domain.project.dto;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class ProjectDetailResponseDto{
    private final Long id;
    private final String title;
    private final String content;
    private final String expectedPeriod;
    private final int viewCount;
    private final int commentCount;
    private final String userNickname;
    private final String recruitmentPeriod;
    private final String recruitStatus;
    private final String teamSize;
    private final String modifiedAt;
    private List<ProjectUrlResponseDto> fileUrls; // 변경 해야하는 값이어서 final x
    private List<TechStackDto> techStacks;

    @QueryProjection
    public ProjectDetailResponseDto(Long id, String title, String content, String expectedPeriod, int viewCount,
                                    int commentCount, String userNickname, String recruitmentPeriod, String recruitStatus,
                                    String teamSize, String modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.expectedPeriod = expectedPeriod;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.userNickname = userNickname;
        this.recruitmentPeriod = recruitmentPeriod;
        this.recruitStatus = recruitStatus;
        this.teamSize = teamSize;
        this.modifiedAt = modifiedAt;
    }

    public void setFileUrls (List<ProjectUrlResponseDto> urls) {
        this.fileUrls = urls;
    }

    public void setTechStacks(List<TechStackDto> techStacks) {
        this.techStacks = techStacks;
    }
}