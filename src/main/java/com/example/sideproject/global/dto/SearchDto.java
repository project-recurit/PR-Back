package com.example.sideproject.global.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

public record SearchDto(
        @Parameter(name = "page", description = "페이지")
        PageDto pageDto,
        @Parameter(name = "dateSort", description = "날짜 정렬", schema = @Schema(implementation = DateSort.class))
        DateSort dateSort
) {
    public SearchDto {
        pageDto = Objects.requireNonNullElse(pageDto, PageDto.create());

        if (dateSort == null) {
            dateSort = DateSort.basicSort();
        }
    }

    public static SearchDto create() {
        return new SearchDto(PageDto.create(), DateSort.basicSort());
    }
}
