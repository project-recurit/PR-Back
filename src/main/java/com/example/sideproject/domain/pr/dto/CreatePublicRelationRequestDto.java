package com.example.sideproject.domain.pr.dto;

import java.util.List;
import java.util.Set;

import com.example.sideproject.domain.pr.entity.PublicRelation;
import com.example.sideproject.domain.user.entity.TechStack1;
import com.example.sideproject.domain.user.entity.User;
import lombok.Builder;

@Builder
public record CreatePublicRelationRequestDto(
        String title,
        List<CreatePublicRelationDetailRequestDto> prDetails,
        Set<TechStack1> techStack1s
) {
    public PublicRelation toEntity(User user) {
        return PublicRelation.builder()
                .user(user)
                .title(title)
                .prDetails(prDetails.stream().map(CreatePublicRelationDetailRequestDto::toEntity).toList())
                .techStack1s(techStack1s)
                .build();
    }
}
