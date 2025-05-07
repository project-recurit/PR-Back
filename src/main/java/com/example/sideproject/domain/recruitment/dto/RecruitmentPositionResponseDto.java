package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.global.enums.Position;
import lombok.Getter;

@Getter
public class RecruitmentPositionResponseDto {
    private final Position position;
    private final int capacity;

    public RecruitmentPositionResponseDto(Position position, int capacity) {
        this.position = position;
        this.capacity = capacity;
    }
}
