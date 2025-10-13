package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.techstack.dto.TechStackMapping;

import java.util.List;
import java.util.function.Function;

public record PrTechStackResponse(
        Long id,
        String name,
        int level
) implements TechStackMapping {
    public PrTechStackResponse(PrTechStackVo mapping) {
        this(mapping.techStackId(), mapping.name(), mapping.level());
    }
}