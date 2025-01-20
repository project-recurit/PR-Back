package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PublicRelationTechStacks;
import com.example.sideproject.domain.user.entity.TechStack;

public record PublicRelationTechStackDto(
        TechStack techStack
) {
    public static PublicRelationTechStackDto of(PublicRelationTechStacks prTechStacks) {
       return new PublicRelationTechStackDto(prTechStacks.getTechStack());
    }
}
