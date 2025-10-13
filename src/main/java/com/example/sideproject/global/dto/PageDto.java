package com.example.sideproject.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ParameterObject
public record PageDto(
        @Schema(name = "page", description = "현재 페이지(0부터 시작), 기본 값 0", type = "integer")
        int page,
        @Schema(name = "size", description = "페이지 크기, 기본 값 10", type = "integer")
        int size
) {
    public PageRequest toPageRequest(Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(page, size);
    }

    public static PageDto create() {
        return new PageDto(0, 10);
    }
}
