package com.example.sideproject.domain.techstack.dto;

import com.example.sideproject.domain.techstack.entity.TechStack;

import java.util.List;
import java.util.stream.Collectors;

public record TechStackDto(
        Long techStackId,
        String name
) {
    public static TechStackDto of(TechStack techStack) {
        return new TechStackDto(techStack.getId(), techStack.getName());
    }

    public static List<TechStackDto> of(List<TechStack> techStacks) {
        return techStacks.stream()
                .map(TechStackDto::of)
                .collect(Collectors.toList());
    }
}
