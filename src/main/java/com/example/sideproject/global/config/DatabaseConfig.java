package com.example.sideproject.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO) // 페이지네이션 할때 직렬화 문제 해소
@EnableJpaRepositories(basePackages = {
        "com.example.sideproject.domain.recruitment.repository",
        "com.example.sideproject.domain.user.repository",
        "com.example.sideproject.domain.applicant.repository",
        "com.example.sideproject.domain.techstack.repository",
        "com.example.sideproject.domain.bookmark.repository",
        "com.example.sideproject.domain.chat.repository",
        "com.example.sideproject.domain.notification.repository",
        "com.example.sideproject.domain.pr.repository",
        "com.example.sideproject.domain.resume.repository"
})
@EnableJpaAuditing
public class DatabaseConfig {
}
