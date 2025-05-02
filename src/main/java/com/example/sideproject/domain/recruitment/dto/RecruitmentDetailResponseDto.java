package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.domain.recruitment.entity.EstimatedDuration;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class RecruitmentDetailResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final EstimatedDuration estimatedDuration;
    private final int viewCount;
    private final int commentCount;
    private final String userNickname;
    private final String deadLine;
    private final boolean isRecruiting;
    private final int recruitmentCapacity;
    private final String modifiedAt;
    private final String workType;
    private List<RecruitmentImageResponseDto> fileUrls; // 변경 해야하는 값이어서 final x
    private List<TechStackDto> techStacks;
    private String estimatedDurationDetail;
    @QueryProjection
    public RecruitmentDetailResponseDto(Long id, String title, String content, EstimatedDuration estimatedDuration, int viewCount,
                                        int commentCount, String userNickname, String deadLine, boolean isRecruiting,
                                        int recruitmentCapacity, String modifiedAt, String workType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.estimatedDuration = estimatedDuration;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.userNickname = userNickname;
        this.deadLine = deadLine;
        this.isRecruiting = isRecruiting;
        this.recruitmentCapacity = recruitmentCapacity;
        this.modifiedAt = modifiedAt;
        this.workType = workType;
    }
    public void setEstimatedDurationDetail(String description) {
        this.estimatedDurationDetail = description;
    }
    public void setFileUrls (List<RecruitmentImageResponseDto> urls) {
        this.fileUrls = urls;
    }

    public void setTechStacks(List<TechStackDto> techStacks) {
        this.techStacks = techStacks;
    }
}