package com.example.sideproject.domain.techstack.dto;

import com.example.sideproject.domain.techstack.entity.TechStack;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

public record TechStackDto(
        @Schema(description = "기술스택 고유번호")
        Long id,
        @Schema(description = "기술스택 명")
        String name
) implements TechStackMapping {
    public static TechStackDto of(TechStack techStack) {
        return new TechStackDto(techStack.getId(), techStack.getName());
    }

    public static List<TechStackDto> of(List<TechStack> techStacks) {
        return techStacks.stream()
                .map(TechStackDto::of)
                .collect(Collectors.toList());
    }

    public static List<TechStackDto> from(List<TechStackMapping> mappings) {
        return mappings.stream()
                .map(t -> new TechStackDto(t.id(), t.name()))
                .toList();
    }
}
