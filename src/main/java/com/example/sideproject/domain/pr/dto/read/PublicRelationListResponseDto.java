package com.example.sideproject.domain.pr.dto.read;

import com.example.sideproject.domain.pr.entity.PublicRelation;
import com.example.sideproject.domain.user.entity.TechStack1;
import lombok.Builder;

import java.util.List;

@Builder
public record PublicRelationListResponseDto(
        Long id,
        String title,
        String username,
        List<TechStack1> techStack1s
) {

    public static PublicRelationListResponseDto of(PublicRelation pr) {
        return PublicRelationListResponseDto.builder()
                .id(pr.getId())
                .title(pr.getTitle())
                .username(pr.getUser().getUsername())
                .techStack1s(pr.getTechStackList())
                .build();
    }

}
