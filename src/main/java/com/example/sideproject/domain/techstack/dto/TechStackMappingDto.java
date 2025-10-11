package com.example.sideproject.domain.techstack.dto;

public record TechStackMappingDto(
        Long id,
        Long techStackId,
        String name
) {
    public static TechStackDto toTechStackDto(TechStackMappingDto mappingDto) {
        return new TechStackDto(mappingDto.techStackId(), mappingDto.name());
    }
}
