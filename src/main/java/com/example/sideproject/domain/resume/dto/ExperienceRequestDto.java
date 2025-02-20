package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.resume.entity.Experience;
import com.example.sideproject.global.validation.time.ValidLocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ExperienceRequestDto(
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
        String achievement,
        @Schema(description = "프로젝트 링크")
        String documentUrl
) {
    public Experience toEntity() {
        return Experience.builder()
                .title(title)
                .startDate(getLocalDateTime(startDate))
                .endDate(getLocalDateTime(endDate))
                .teamSize(teamSize)
                .achievement(achievement)
                .documentUrl(documentUrl)
                .build();
    }

    private LocalDateTime getLocalDateTime(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(str, formatter);
    }
}
