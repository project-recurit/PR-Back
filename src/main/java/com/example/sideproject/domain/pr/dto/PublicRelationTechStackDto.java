package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PublicRelationTechStacks;
import com.example.sideproject.domain.user.entity.TechStack1;

public record PublicRelationTechStackDto(
        TechStack1 techStack1
) {
    public static PublicRelationTechStackDto of(PublicRelationTechStacks prTechStacks) {
       return new PublicRelationTechStackDto(prTechStacks.getTechStack1());
    }
}
