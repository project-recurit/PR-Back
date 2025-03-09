package com.example.sideproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO) // 페이지네이션 할때 직렬화 문제 해소
@EnableJpaRepositories(basePackages = {
        "com.example.sideproject.domain.project.repository",
        "com.example.sideproject.domain.user.repository",
        "com.example.sideproject.domain.applicant.repository",
        "com.example.sideproject.domain.techstack.repository",
        "com.example.sideproject.domain.bookmark.repository",
        "com.example.sideproject.domain.chat.repository",
        "com.example.sideproject.domain.comment.repository",
        "com.example.sideproject.domain.notification.repository",
        "com.example.sideproject.domain.pr.repository",
        "com.example.sideproject.domain.resume.repository"
})
//@EnableElasticsearchRepositories(basePackages = "com.example.sideproject.domain.search.repository")
public class SideprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SideprojectApplication.class, args);
    }

}
