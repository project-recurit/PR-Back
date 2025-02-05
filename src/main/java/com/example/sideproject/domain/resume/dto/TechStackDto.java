package com.example.sideproject.domain.resume.dto;

import com.example.sideproject.domain.techstack.TechStack;

public record TechStackDto(
        Long techStackId,
        String name
) {
    public static TechStackDto of(TechStack techStack) {
        return new TechStackDto(techStack.getId(), techStack.getName());
    }
}
