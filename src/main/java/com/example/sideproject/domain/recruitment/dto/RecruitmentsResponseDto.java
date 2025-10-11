package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.domain.recruitment.entity.RecruitmentCategory;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.dto.TechStackMapping;
import com.example.sideproject.global.entity.Timestamped;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class RecruitmentsResponseDto {
    private final Long id;
    private final String title;
    private final String nickname;
    private final int viewCount;
    private final int commentCount;
    private final String modifiedAt;
    private final RecruitmentCategory recruitmentCategory;
    private final boolean isCommercial;
    private List<TechStackDto> techStacks;

    @QueryProjection
    public RecruitmentsResponseDto(Long id, String title, String nickname, int viewCount, int commentCount, LocalDateTime modifiedAt, RecruitmentCategory recruitmentCategory, boolean isCommercial) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.modifiedAt = modifiedAt.toString();
        this.recruitmentCategory = recruitmentCategory;
        this.isCommercial = isCommercial;
        this.techStacks = Collections.emptyList();
    }
    @QueryProjection
    public RecruitmentsResponseDto(Long id, String title, String nickname, int viewCount, int commentCount, String modifiedAt, RecruitmentCategory recruitmentCategory, boolean isCommercial, List<TechStackDto> techStacks) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.modifiedAt = modifiedAt;
        this.recruitmentCategory = recruitmentCategory;
        this.isCommercial = isCommercial;
        this.techStacks = techStacks;
    }

    public void setTechStacks(List<TechStackMapping> techStacks) {
        this.techStacks = TechStackDto.from(techStacks);
    }
}