package com.example.sideproject.domain.project.service;

import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectService projectService;

    @Test
    void findUserByTechStacks() {
        List<TechStack> techStacks = List.of(
                new TechStack(99L, "C"),
                new TechStack(100L, "C++"),
                new TechStack(101L, "C#"),
                new TechStack(102L, "JAVA")
        );

        List<User> users = projectService.findUserByTechStacks(techStacks);

        System.out.println(users.size());
        System.out.println(users.get(0).getEmail());
    }
}