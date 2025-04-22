package com.example.sideproject.domain.pr.dto;

public record PrTechStackMapping(
        Long prId,
        Long techStackId,
        String name,
        int level
    ) {}