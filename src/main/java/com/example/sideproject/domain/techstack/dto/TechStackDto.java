package com.example.sideproject.domain.techstack.dto;

import com.example.sideproject.domain.techstack.entity.TechStack;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

public record TechStackDto(
        @Schema(name = "기술스택 고유번호")
        Long id,
        @Schema(name = "기술스택 명")
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
