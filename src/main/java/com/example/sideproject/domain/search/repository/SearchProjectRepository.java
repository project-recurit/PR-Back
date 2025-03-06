package com.example.sideproject.domain.search.repository;

import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.search.entity.ProjectDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchProjectRepository extends ElasticsearchRepository<ProjectDocument, Long>{
    List<ProjectDocument> findByTitleContainingOrContentContainingOrTechStackNamesContaining(
            String title, String content, String techStackNames);

    List<ProjectDocument> findByTechStackNamesIn(List<String> techStackNames);

    List<ProjectDocument> findByTitleContainingOrContentContainingAndTechStackNamesIn(
            String title, String content, List<String> techStackNames);
}
