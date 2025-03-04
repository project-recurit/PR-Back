package com.example.sideproject.domain.search.service;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.search.dto.ProjectDocument;
import com.example.sideproject.domain.search.dto.SearchResultDto;
import com.example.sideproject.domain.search.repository.SearchProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final SearchProjectRepository searchProjectRepository;

    public List<SearchResultDto> search(String query, String type) {
        List<SearchResultDto> results = new ArrayList<>();

        if (type == null || type.equalsIgnoreCase("project")) {
            List<ProjectDocument> projects = searchProjectRepository.findByTitleContainingOrContentContaining(query, query);
            log.info("검색 쿼리 '{}' 결과: {} 개의 프로젝트 찾음", query, projects.size());

            results.addAll(projects.stream()
                    .map(SearchResultDto::fromProjectDocument)
                    .toList());
        }
        //todo pr 검색도 추가

        return results;
    }

    //엘라스틱 서치 동기화
    public void saveProject(Project project) {
        ProjectDocument projectDocument = ProjectDocument.fromProject(project);
        searchProjectRepository.save(projectDocument);
        log.info("프로젝트 ID {} Elasticsearch에 인덱싱 완료", project.getId());
    }
}
