package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Experience;
import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.global.validation.time.ValidLocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ExperienceRequest(
        @Schema(description = "제목")
        String title,
        @Schema(description = "시작 날짜, yyyy-MM-dd'T'HH:mm:ss 형식 값")
        @ValidLocalDateTime
        String startDate,
        @Schema(description = "종료 날짜, yyyy-MM-dd'T'HH:mm:ss 형식 값")
        @ValidLocalDateTime
        String endDate,
        @Schema(description = "참여 인원")
        int teamSize,
        @Schema(description = "담당 업무 및 성과")
        String description,
        @Schema(description = "프로젝트 링크")
        String documentUrl
) {
    public Experience toEntity() {
        return Experience.builder()
                .title(title)
                .startDate(Timestamped.toLocalDateTime(startDate))
                .endDate(Timestamped.toLocalDateTime(endDate))
                .teamSize(teamSize)
                .description(description)
                .documentUrl(documentUrl)
                .build();
    }

}
