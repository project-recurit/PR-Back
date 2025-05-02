package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public record PrSearchRequest(
        @Schema(description = "포지션")
        List<Position> positions,
        @Schema(description = "기술 스택 고유번호")
        List<Long> techStackIds,
        @Schema(description = "진행 방식")
        List<WorkType> workTypes,
        @Schema(description = "검색 텍스트, 제목과 소개 내용 검색")
        String searchText
) {
    public PrSearchRequest {
        // 중복 제거
        if (positions == null) {
            positions = new ArrayList<>();
        }

        if (techStackIds == null) {
            techStackIds = new ArrayList<>();
        }

        if (workTypes == null) {
            workTypes = new ArrayList<>();
        }

        positions = new HashSet<>(positions).stream().toList();
        techStackIds = new HashSet<>(techStackIds).stream().toList();
        workTypes = new HashSet<>(workTypes).stream().toList();
    }
}
