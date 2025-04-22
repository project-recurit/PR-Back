package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PrTechStack;
import com.example.sideproject.domain.techstack.entity.TechStack;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PrTechStackRequest(
            @Schema(description = "기술스택 고유번호")
            Long id,
            @Min(1)
            @Max(10)
            @Schema(description = "기술 스택 레벨 (1~10)")
            int level
    ) {
        public PrTechStack toEntity() {
            return PrTechStack.builder()
                    .techStack(new TechStack(id))
                    .level(level)
                    .build();
        }
    }