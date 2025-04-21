package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PrExperience;
import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.global.validation.time.ValidLocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

public record PrExperienceRequest(
            @Schema(description = "제목")
            String title,
            @Schema(description = "담당 업무 및 성과")
            String description,
            @Schema(description = "참여 인원")
            int teamSize,
            @ValidLocalDateTime
            @Schema(description = "시작 날짜, yyyy-MM-dd'T'HH:mm:ss 형식 값")
            String startDate,
            @ValidLocalDateTime
            @Schema(description = "시작 날짜, yyyy-MM-dd'T'HH:mm:ss 형식 값")
            String endDate,
            @Schema(description = "프로젝트 링크")
            String documentUrl
    ) {
        public PrExperience toEntity() {
            return PrExperience.builder()
                    .title(title)
                    .description(description)
                    .teamSize(teamSize)
                    .startDate(Timestamped.toLocalDateTime(startDate))
                    .endDate(Timestamped.toLocalDateTime(endDate))
                    .documentUrl(documentUrl)
                    .build();
        }
    }