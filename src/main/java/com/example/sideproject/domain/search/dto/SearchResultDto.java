package com.example.sideproject.domain.search.dto;

import com.example.sideproject.domain.search.entity.ProjectDocument;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResultDto {
    private Long id;
    private String title;
    private String content;
    private String type;

    public static SearchResultDto fromProjectDocument(ProjectDocument document) {
        return SearchResultDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .content(document.getContent())
                .type("project")
                .build();
    }
}
