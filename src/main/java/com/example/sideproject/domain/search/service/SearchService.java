package com.example.sideproject.domain.search.service;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.search.entity.PostSearchType;
import com.example.sideproject.domain.search.entity.ProjectDocument;
import com.example.sideproject.domain.search.dto.SearchResultDto;
import com.example.sideproject.domain.search.repository.SearchProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final SearchProjectRepository searchProjectRepository;

    public List<SearchResultDto> search(String query, PostSearchType type) {
        List<SearchResultDto> results = new ArrayList<>();

        return switch (type) {
            case project -> searchProject(query);
            //todo pr 검색도 추가
            case pr -> results;
        };
    }

    private List<SearchResultDto> searchProject(String query) {
        List<ProjectDocument> projects = searchProjectRepository.findByTitleContainingOrContentContaining(query, query);
        log.info("검색 쿼리 '{}' 결과: {} 개의 프로젝트 찾음", query, projects.size());

        return projects.stream()
                .map(SearchResultDto::fromProjectDocument)
                .toList();
    }


    //엘라스틱 서치 동기화
    public void saveProject(Project project) {
        ProjectDocument projectDocument = new ProjectDocument(project);
        searchProjectRepository.save(projectDocument);
        log.info("프로젝트 ID {} Elasticsearch에 인덱싱 완료", project.getId());
    }
}
