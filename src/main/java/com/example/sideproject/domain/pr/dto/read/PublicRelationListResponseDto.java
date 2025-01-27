package com.example.sideproject.domain.pr.dto.read;

import com.example.sideproject.domain.pr.entity.PublicRelation;
import com.example.sideproject.domain.pr.entity.PublicRelationTechStacks;
import com.example.sideproject.domain.user.entity.TechStack;
import lombok.Builder;

import java.util.List;

@Builder
public record PublicRelationListResponseDto(
        Long id,
        String title,
        String username,
        List<TechStack> techStacks
) {

    public static PublicRelationListResponseDto of(PublicRelation pr) {
        return PublicRelationListResponseDto.builder()
                .id(pr.getId())
                .title(pr.getTitle())
                .username(pr.getUser().getUsername())
                .techStacks(pr.getTechStackList())
                .build();
    }

}
