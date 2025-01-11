package com.example.sideproject.domain.pr.dto.read;

public record PublicRelationReadDto(
        Long id,
        String title,
        String username
) {
    public record TechStack(
            Long prId,
            TechStack techStack
    ) {

    }
}
