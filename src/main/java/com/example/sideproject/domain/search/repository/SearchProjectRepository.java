package com.example.sideproject.domain.search.repository;

import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.search.entity.ProjectDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchProjectRepository extends ElasticsearchRepository<ProjectDocument, Long>{
    // 기본 검색 (제목/내용/기술 스택에서 검색)
    List<ProjectDocument> findByTitleContainingOrContentContainingOrTechStackNamesContaining(
            String title, String content, String techStackNames);

    // 기술 스택으로만 필터링
    List<ProjectDocument> findByTechStackNamesIn(List<String> techStackNames);

    // 제목/내용 검색 + 기술 스택 필터링
    List<ProjectDocument> findByTitleContainingOrContentContainingAndTechStackNamesIn(
            String title, String content, List<String> techStackNames);
}
