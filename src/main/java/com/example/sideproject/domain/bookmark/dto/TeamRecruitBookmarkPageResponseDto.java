package com.example.sideproject.domain.bookmark.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import lombok.Getter;
import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;


@Getter
public class TeamRecruitBookmarkPageResponseDto {
    private final List<TeamRecruitBookmarkResponseDto> bookmarks;
    private final int currentPage;
    private final int totalPages;
    private final long totalElements;
    private final boolean hasNext;

    public TeamRecruitBookmarkPageResponseDto(Page<TeamRecruitBookmark> bookmarkPage) {
        this.bookmarks = bookmarkPage.getContent().stream()
                .map(TeamRecruitBookmarkResponseDto::new)
                .collect(Collectors.toList());
        this.currentPage = bookmarkPage.getNumber();
        this.totalPages = bookmarkPage.getTotalPages();
        this.totalElements = bookmarkPage.getTotalElements();
        this.hasNext = bookmarkPage.hasNext();
    }
} 