package com.example.sideproject.domain.recruitment.dto;


import com.example.sideproject.domain.recruitment.entity.EstimatedDuration;
import com.example.sideproject.domain.recruitment.entity.RecruitmentCategory;
import com.example.sideproject.global.enums.WorkType;
import lombok.Getter;

import java.util.List;

@Getter
public class RecruitmentRequestDto {

    private String title;
    private String content;
    private String deadLine;
    private EstimatedDuration estimatedDuration;
    private WorkType workType;
    private RecruitmentCategory recruitmentCategory;
    private boolean isCommercial;
    private List<Long> techStackIds;

    public RecruitmentRequestDto() {
    }

    public RecruitmentRequestDto(String title, String content, String deadLine, EstimatedDuration estimatedDuration,
                                 WorkType workType, RecruitmentCategory recruitmentCategory, boolean isCommercial,List<Long> techStackIds) {
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.estimatedDuration = estimatedDuration;
        this.workType = workType;
        this.recruitmentCategory = recruitmentCategory;
        this.isCommercial = isCommercial;
        this.techStackIds = techStackIds;
    }
}