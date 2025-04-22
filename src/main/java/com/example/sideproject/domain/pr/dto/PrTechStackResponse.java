package com.example.sideproject.domain.pr.dto;

public record PrTechStackResponse(
        Long id,
        String name,
        int level
) {
    public PrTechStackResponse(PrTechStackMapping mapping) {
        this(mapping.techStackId(), mapping.name(), mapping.level());
    }
}