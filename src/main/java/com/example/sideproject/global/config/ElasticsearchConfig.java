package com.example.sideproject.global.config;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.repository.ProjectRepository;
import com.example.sideproject.domain.search.dto.ProjectDocument;
import com.example.sideproject.domain.search.repository.SearchProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchConfig {

    private final ProjectRepository projectRepository;
    private final SearchProjectRepository searchProjectRepository;

    @Bean
    //서버 실행될 때 JPA에서 인덱싱한걸 엘라스틱 서치에 저장
    public CommandLineRunner indexProjectsOnStartup() {
        return args -> {
            try {
                long count = searchProjectRepository.count();
                if (count == 0) {
                    log.info("Elasticsearch에 인덱싱된 프로젝트가 없습니다. 초기 인덱싱을 시작합니다...");
                    List<Project> allProjects = projectRepository.findAll();
                    List<ProjectDocument> documents = allProjects.stream()
                                    .map(ProjectDocument::fromProject)
                            .toList();
                    searchProjectRepository.saveAll(documents);
                    log.info("초기 데이터 인덱싱 완료: {} 개의 프로젝트 인덱싱됨", allProjects.size());
                } else {
                    log.info("이미 {} 개의 프로젝트가 인덱싱되어 있습니다.", count);
                }
            } catch (Exception e) {
                log.error("초기 데이터 인덱싱 중 오류 발생: {}", e.getMessage(), e);
            }
        };
    }
}
