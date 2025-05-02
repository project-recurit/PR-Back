package com.example.sideproject.domain.recruitment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class RecruitmentTechStackDto {
    private final Long recruitmentId;
    private final Long techStackId;
    private final String name;

    @QueryProjection
    public RecruitmentTechStackDto(Long recruitmentId, Long techStackId, String name) {
        this.recruitmentId = recruitmentId;
        this.techStackId = techStackId;
        this.name = name;
    }
}
