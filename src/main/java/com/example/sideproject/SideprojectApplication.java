package com.example.sideproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO) // 페이지네이션 할때 직렬화 문제 해소
public class SideprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SideprojectApplication.class, args);
    }

}
