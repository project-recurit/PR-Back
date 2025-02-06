//package com.example.sideproject.domain.project.service;
//
//import com.example.sideproject.domain.project.entity.Project;
//import com.example.sideproject.domain.techstack.entity.TechStack;
//import com.example.sideproject.domain.user.entity.User;
//import com.example.sideproject.domain.user.entity.UserTechStack;
//import com.example.sideproject.global.notification.dto.EventListDto;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class ProjectNoticeServiceTest {
//
//    @Autowired
//    ProjectNoticeService projectNoticeService;
//
//    @Test
//    void notice() {
//        TechStack c = new TechStack(99L, "C");
//        TechStack cplusplus = new TechStack(100L, "C++");
//        TechStack csyap= new TechStack(101L, "C#");
//        TechStack java = new TechStack(102L, "JAVA");
//        List<TechStack> techStacks = List.of(
//                c,cplusplus,csyap,java
//        );
//
//        User user1 = User.builder()
//                .id(1L)
//                .userTechStacks(List.of(
//                        new UserTechStack(1L, c, null),
//                        new UserTechStack(1L, cplusplus, null))
//                )
//                .build();
//
//        List<User> users = List.of(
//                user1,
//                User.builder()
//                        .id(2L)
//                        .userTechStacks(List.of(
//                                new UserTechStack(1L, cplusplus, null),
//                                new UserTechStack(1L, java, null))
//                        )
//                        .build()
//        );
//
//        Project project = Project.builder()
//                .id(1L)
//                .title("project1")
//                .user(user1)
//                .build();
//        List<Long> techStackIds = techStacks.stream().map(TechStack::getId).toList();
//        EventListDto notice = projectNoticeService.notice(project, users, techStackIds);
//        System.out.println(notice);
//    }
//}