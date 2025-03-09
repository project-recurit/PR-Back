package com.example.sideproject.global.config;

import com.example.sideproject.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchConfig {

    private final SearchService searchService;

    @Bean
    //서버 실행될 때 JPA에서 인덱싱한걸 엘라스틱 서치에 저장
    public CommandLineRunner indexProjectsOnStartup() {
        return args -> {
            try {
                searchService.initDocuments();
            } catch (Exception e) {
                log.error("초기 데이터 인덱싱 중 오류 발생: {}", e.getMessage(), e);
            }
        };
    }
}
