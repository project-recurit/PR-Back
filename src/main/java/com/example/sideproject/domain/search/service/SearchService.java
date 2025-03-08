package com.example.sideproject.domain.search.service;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.project.repository.ProjectRepository;
import com.example.sideproject.domain.search.entity.PostSearchType;
import com.example.sideproject.domain.search.entity.ProjectDocument;
import com.example.sideproject.domain.search.dto.SearchResultDto;
import com.example.sideproject.domain.search.repository.SearchProjectRepository;
import com.example.sideproject.domain.techstack.entity.TechStack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final SearchProjectRepository searchProjectRepository;
    private final ProjectRepository projectRepository;

    public List<SearchResultDto> search(String query, PostSearchType type, List<String> techStacks) {
        List<SearchResultDto> results = new ArrayList<>();

        return switch (type) {
            case all -> results;

            case project -> searchProject(query,techStacks);
            //todo pr 검색도 추가
            case pr -> results;


        };
    }

    private List<SearchResultDto> searchProject(String query, List<String> techStacks) {
        if ((query == null || query.trim().isEmpty()) && (techStacks == null || techStacks.isEmpty())) {
            log.warn("검색 쿼리와 기술 스택이 모두 비어있습니다.");
            return new ArrayList<>();
        }

        List<ProjectDocument> projects;
        if (techStacks != null && !techStacks.isEmpty()) {
            techStacks = techStacks.stream()
                    .filter(tech -> tech != null && !tech.trim().isEmpty())
                    .map(String::trim)  // 공백 제거
                    .toList();
            if (query != null && !query.trim().isEmpty()) {
                projects = searchProjectRepository.findByTitleContainingOrContentContainingAndTechStackNamesIn(
                        query, query, techStacks);
            } else {
                projects = searchProjectRepository.findByTechStackNamesIn(techStacks);  // OR 조건
            }
        } else {
            projects = searchProjectRepository.findByTitleContainingOrContentContainingOrTechStackNamesContaining(
                    query, query, query);
        }
        log.info("검색 쿼리 '{}', 기술 스택 '{}': {} 개의 프로젝트 찾음", query, techStacks, projects.size());
        return projects.stream()
                .map(SearchResultDto::fromProjectDocument)
                .toList();
    }


    //엘라스틱 서치 동기화
    public void saveProject(Project project) {
        ProjectDocument projectDocument = new ProjectDocument(project);
        searchProjectRepository.save(projectDocument);
        log.info("프로젝트 ID {} Elasticsearch 인덱싱 완료", project.getId());
    }

    @Transactional
    public void initDocuments() {
        try {
            searchProjectRepository.deleteAll();
            long count = searchProjectRepository.count();
            if (count == 0) {
                log.info("Elasticsearch 인덱싱된 프로젝트가 없습니다. 초기 인덱싱을 시작합니다...");
                List<Project> allProjects = projectRepository.findAll();
                List<ProjectDocument> documents = allProjects.stream()
                        .map(ProjectDocument::new)
                        .toList();
                searchProjectRepository.saveAll(documents);
                log.info("초기 데이터 인덱싱 완료: {} 개의 프로젝트 인덱싱됨", allProjects.size());
            } else {
                log.info("이미 {} 개의 프로젝트가 인덱싱되어 있습니다.", count);
            }
        } catch (Exception e) {
            log.error("초기 데이터 인덱싱 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("Elasticsearch 초기 인덱싱 실패", e);
        }
    }
}

