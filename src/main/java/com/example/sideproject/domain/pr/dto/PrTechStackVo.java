package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.techstack.dto.BasicTechStack;

public record PrTechStackVo(
        Long prId,
        Long techStackId,
        String name,
        int level
) implements BasicTechStack {
    @Override
    public Long id() {
        return prId;
    }
}