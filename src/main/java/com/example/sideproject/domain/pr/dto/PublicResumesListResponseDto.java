package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.global.enums.WorkType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.List;

@Getter
public class PublicResumesListResponseDto {
    private final Long publicResumeId;
    @JsonIgnore
    private final Long resumeId;
    private final String title;
    private List<String> techStacks;
    private final WorkType workType;
    private final int viewCount;
    private final int commentCount;
    private final int favoriteCount;

    public PublicResumesListResponseDto(Long publicResumeId, Long resumeId, String title, WorkType workType, int viewCount, int commentCount, int favoriteCount) {
        this.publicResumeId = publicResumeId;
        this.resumeId = resumeId;
        this.title = title;
        this.workType = workType;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
    }

    public PublicResumesListResponseDto addTechStacks(List<String> techStacks) {
        this.techStacks = techStacks;
        return this;
    }
}
