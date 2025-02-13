package com.example.sideproject.domain.project.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProjectDetailResponseDto(
        ProjectResponseDto response,
        List<ProjectUrlResponseDto> urls,
        List<ProjectTechStackResponseDto> techStacks
) {
}
