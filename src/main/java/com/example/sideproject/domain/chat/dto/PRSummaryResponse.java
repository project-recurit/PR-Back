package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.pr.entity.PublicResumes;

public record PRSummaryResponse(
        Long id,
        String resumeTitle
) {
    public static PRSummaryResponse from(PublicResumes pr) {
        return new PRSummaryResponse(
                pr.getId(),
                pr.getResume().getTitle()
        );
    }
}