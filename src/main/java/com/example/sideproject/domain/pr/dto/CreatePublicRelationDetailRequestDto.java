package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PublicRelationDetail;

public record CreatePublicRelationDetailRequestDto(
        String title,
        String description
) {
    public PublicRelationDetail toEntity() {
        return PublicRelationDetail.builder()
                .title(title)
                .description(description)
                .build();
    }
}
