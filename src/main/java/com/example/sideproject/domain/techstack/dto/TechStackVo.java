package com.example.sideproject.domain.techstack.dto;

public record TechStackVo(
        Long id,
        Long techStackId,
        String name
) implements TechStackMapping, BasicTechStack {

    public static <T extends BasicTechStack> TechStackMapping toBasicDto(T mappingDto) {
        return new TechStackDto(mappingDto.techStackId(), mappingDto.name());
    }

}
