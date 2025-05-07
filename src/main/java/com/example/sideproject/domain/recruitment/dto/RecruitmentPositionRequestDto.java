package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.global.enums.Position;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RecruitmentPositionRequestDto {
    @NotNull
    private int capacity;
    @NotNull
    private Position position;
}
