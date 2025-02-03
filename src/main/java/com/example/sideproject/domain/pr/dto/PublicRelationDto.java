package com.example.sideproject.domain.pr.dto;

import java.util.List;

import com.example.sideproject.domain.pr.entity.PublicRelation;
import com.example.sideproject.domain.user.entity.TechStack1;

import lombok.Builder;

@Builder
public record PublicRelationDto(
        Long id,
        String title,
        List<PublicRelationDetailDto> prDetails,
        List<TechStack1> techStack1s
) {
    public static PublicRelationDto of(PublicRelation pr) {
        return PublicRelationDto.builder()
                .id(pr.getId())
                .title(pr.getTitle())
                .techStack1s(pr.getTechStackList())
                .prDetails(pr.getPrDetails().stream().map(PublicRelationDetailDto::of).toList())
                .build();
    }
}