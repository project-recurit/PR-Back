package com.example.sideproject.domain.search.controller;


import com.example.sideproject.domain.search.dto.SearchResultDto;
import com.example.sideproject.domain.search.entity.PostSearchType;
import com.example.sideproject.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class SearchController {


    private final SearchService searchService;

    @GetMapping("/search")
    public List<SearchResultDto> search(@RequestParam String query,
                                        @RequestParam(required = false) PostSearchType type,
                                        @RequestParam(required = false) String keyword) {
        return searchService.search(query, type, keyword);
    }
}
