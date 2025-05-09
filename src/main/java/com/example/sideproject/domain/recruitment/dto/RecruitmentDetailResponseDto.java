package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.domain.recruitment.entity.EstimatedDuration;
import com.example.sideproject.domain.recruitment.entity.RecruitmentCategory;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.global.dto.PostResponseDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class RecruitmentDetailResponseDto extends PostResponseDto {
    private final String content;
    private final EstimatedDuration estimatedDuration;
    private final String deadLine;
    private final boolean isRecruiting;
    private final String workType;
    private final RecruitmentCategory recruitmentCategory;
    private final boolean isCommercial;

    private List<RecruitmentImageResponseDto> fileUrls; // 변경 필요 값
    private List<TechStackDto> techStacks;
    private List<RecruitmentPositionResponseDto> positions;
    private String estimatedDurationDetail;

    @QueryProjection
    public RecruitmentDetailResponseDto(
            Long id, String title, String nickname, String profileUrl,
            int viewCount, int commentCount, int favoriteCount,
            String createdAt, String modifiedAt,
            String content, EstimatedDuration estimatedDuration,
            String deadLine, boolean isRecruiting,
            String workType, RecruitmentCategory recruitmentCategory, boolean isCommercial) {

        super(id, title, nickname, profileUrl, viewCount, commentCount, favoriteCount, createdAt, modifiedAt);
        this.content = content;
        this.estimatedDuration = estimatedDuration;
        this.deadLine = deadLine;
        this.isRecruiting = isRecruiting;
        this.workType = workType;
        this.recruitmentCategory = recruitmentCategory;
        this.isCommercial = isCommercial;
    }

    public void setEstimatedDurationDetail(String description) {
        this.estimatedDurationDetail = description;
    }

    public void setFileUrls(List<RecruitmentImageResponseDto> urls) {
        this.fileUrls = urls;
    }

    public void setTechStacks(List<TechStackDto> techStacks) {
        this.techStacks = techStacks;
    }

    public void setRecruitPositions(List<RecruitmentPositionResponseDto> positions) {
        this.positions = positions;
    }
}
