package com.example.sideproject.domain.pr.dto;

import java.util.List;
import java.util.Set;

import com.example.sideproject.domain.pr.entity.PublicRelation;
import com.example.sideproject.domain.user.entity.TechStack;

import lombok.Builder;
import lombok.Getter;

@Builder
public record PublicRelationDto(
        Long id,
        String title,
        List<PublicRelationDetailDto> prDetails,
        Set<TechStack> techStacks
) {
    public static PublicRelationDto of(PublicRelation pr) {
        return PublicRelationDto.builder()
                .id(pr.getId())
                .title(pr.getTitle())
                .techStacks(pr.getTechStacks())
                .prDetails(pr.getPrDetails().stream().map(PublicRelationDetailDto::of).toList())
                .build();
    }
}