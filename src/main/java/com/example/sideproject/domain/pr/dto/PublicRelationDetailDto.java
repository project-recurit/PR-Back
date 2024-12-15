package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.PublicRelationDetail;
import lombok.Builder;

@Builder
public record PublicRelationDetailDto(
        Long id,
        String title,
        String description
) {
    public PublicRelationDetail toEntity() {
        return PublicRelationDetail.builder()
                .title(title)
                .description(description)
                .build();
    }

    public static PublicRelationDetailDto of(PublicRelationDetail detail) {
       return PublicRelationDetailDto.builder()
               .id(detail.getId())
               .title(detail.getTitle())
               .description(detail.getDescription())
               .build();
    }
}
