package com.example.sideproject.domain.project.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProjectTechStackDto {
    private final Long projectId;
    private final Long techStackId;
    private final String name;

    @QueryProjection
    public ProjectTechStackDto(Long projectId, Long techStackId, String name) {
        this.projectId = projectId;
        this.techStackId = techStackId;
        this.name = name;
    }
}
