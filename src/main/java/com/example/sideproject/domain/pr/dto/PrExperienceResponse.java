package com.example.sideproject.domain.pr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "PR의 경험(프로젝트) 응답")
@Builder
public record PrExperienceResponse(
        @Schema(description = "pr의 경험(프로젝트) 고유번호")
        Long id,
        @Schema(description = "제목")
        String title,
        @Schema(description = "내용")
        String description,
        @Schema(description = "팀원 수")
        int teamSize,
        @Schema(description = "시작일")
        LocalDateTime startDate,
        @Schema(description = "종료일")
        LocalDateTime endDate,
        @Schema(description = "프로젝트 문서 링크 등")
        String documentUrl,
        @Schema(description = "생성일")
        String createdAt,
        @Schema(description = "수정일")
        String modifiedAt
) {
}
