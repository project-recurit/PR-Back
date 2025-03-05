package com.example.sideproject.domain.search.repository;

import com.example.sideproject.domain.search.entity.ProjectDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchProjectRepository extends ElasticsearchRepository<ProjectDocument, Long>{
    List<ProjectDocument> findByTitleContainingOrContentContaining(String title, String content);
}
