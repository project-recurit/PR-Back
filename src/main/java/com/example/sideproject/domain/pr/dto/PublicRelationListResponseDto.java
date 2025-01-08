package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PublicRelation;
import com.example.sideproject.domain.user.entity.TechStack;
import lombok.Builder;

import java.util.Set;

@Builder
public record PublicRelationListResponseDto(
        Long id,
        String title,
        String username,
        Set<TechStack> techStacks
) {
    public static PublicRelationListResponseDto of(PublicRelation pr) {
        return PublicRelationListResponseDto.builder()
                .id(pr.getId())
                .title(pr.getTitle())
                .username(pr.getUser().getUsername())
                .techStacks(pr.getTechStacks())
                .build();
    }
}
