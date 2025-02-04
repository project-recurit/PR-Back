package com.example.sideproject.domain.project.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class CreateTeamRecruitPageResponseDto {
    private final List<CreateTeamRecruitResponseDto> teamRecruits;
    private final int currentPage;
    private final int totalPages;
    private final long totalElements;
    private final boolean hasNext;
    private final boolean hasPrevious;

    public CreateTeamRecruitPageResponseDto(
            List<CreateTeamRecruitResponseDto> teamRecruits, 
            int currentPage,
            int totalPages, 
            long totalElements) {
        this.teamRecruits = teamRecruits;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.hasNext = currentPage < totalPages - 1;
        this.hasPrevious = currentPage > 0;
    }
}
