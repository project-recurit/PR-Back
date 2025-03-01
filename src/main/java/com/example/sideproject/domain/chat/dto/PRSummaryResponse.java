package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.pr.entity.PublicResumes;

public record PRSummaryResponse(
        Long id,
        Long resumeId,
        String resumeTitle  // 또는 다른 필요한 필드들
) {
    public static PRSummaryResponse from(PublicResumes pr) {
        return new PRSummaryResponse(
                pr.getId(),
                pr.getResume().getId(),  // PublicResumes 클래스에 맞게 필드명 조정 필요
                pr.getResume().getTitle()  // PublicResumes 클래스에 맞게 필드명 조정 필요
        );
    }
}