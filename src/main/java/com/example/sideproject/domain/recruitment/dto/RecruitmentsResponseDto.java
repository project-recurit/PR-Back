package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.domain.recruitment.entity.RecruitmentCategory;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class RecruitmentsResponseDto {
    private final Long id;
    private final String title;
    private final String userNickname;
    private final int viewCount;
    private final int commentCount;
    private final String modifiedAt;
    private final RecruitmentCategory recruitmentCategory;
    private final boolean isCommercial;
    private final List<TechStackDto> techStacks;

    @QueryProjection
    public RecruitmentsResponseDto(Long id, String title, String userNickname, int viewCount, int commentCount, String modifiedAt, RecruitmentCategory recruitmentCategory, boolean isCommercial) {
        this.id = id;
        this.title = title;
        this.userNickname = userNickname;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.modifiedAt = modifiedAt;
        this.recruitmentCategory = recruitmentCategory;
        this.isCommercial = isCommercial;
        this.techStacks = Collections.emptyList();
    }
    @QueryProjection
    public RecruitmentsResponseDto(Long id, String title, String userNickname, int viewCount, int commentCount, String modifiedAt, RecruitmentCategory recruitmentCategory, boolean isCommercial, List<TechStackDto> techStacks) {
        this.id = id;
        this.title = title;
        this.userNickname = userNickname;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.modifiedAt = modifiedAt;
        this.recruitmentCategory = recruitmentCategory;
        this.isCommercial = isCommercial;
        this.techStacks = techStacks;
    }
}