package com.example.sideproject.domain.pr.dto;

import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import java.util.List;

public record PrRequest(
        @Schema(description = "제목")
        String title,
        @Schema(description = "소개")
        String introduce,
        @Schema(description = "직무")
        Position position,
        @Schema(description = "선호 방식")
        WorkType workType,
        @Schema(description = "파일 링크")
        List<String> documentUrl,
        @Valid
        List<PrExperienceRequest> experiences,
        @Valid
        List<PrTechStackRequest> techStacks
) {
    public Pr toEntity(User user) {
        return Pr.builder()
                .user(user)
                .title(title)
                .introduce(introduce)
                .position(position)
                .workType(workType)
                .documentUrl(documentUrl)
                .experiences(experiences.stream().map(PrExperienceRequest::toEntity).toList())
                .techStacks(techStacks.stream().map(PrTechStackRequest::toEntity).toList())
                .build();
    }

}
