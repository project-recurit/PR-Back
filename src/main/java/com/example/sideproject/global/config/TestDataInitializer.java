//package com.example.sideproject.global.config;
//
//import com.example.sideproject.domain.user.entity.User;
//import com.example.sideproject.domain.team.entity.TeamRecruit;
//import com.example.sideproject.domain.personal.entity.ProjectRecruit;
//import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;
//import com.example.sideproject.domain.user.repository.UserRepository;
//import com.example.sideproject.domain.team.repository.TeamRecruitRepository;
//import com.example.sideproject.domain.personal.repository.ProjectRecruitRepository;
//import com.example.sideproject.domain.bookmark.repository.TeamRecruitBookmarkRepository;
//import com.example.sideproject.global.util.TestDataGenerator;
//
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.annotation.Profile;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.Executor;
//
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//@Profile("local")
//public class TestDataInitializer {
//    private final UserRepository userRepository;
//    private final TeamRecruitRepository teamRecruitRepository;
//    private final ProjectRecruitRepository projectRecruitRepository;
//    private final TeamRecruitBookmarkRepository bookmarkRepository;
//    private final Random random = new Random();
//    private final Executor taskExecutor;
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void initializeTestData() {
//        if (isDataExists()) {
//            log.info("테스트 데이터가 이미 존재합니다.");
//            return;
//        }
//
//        CompletableFuture.runAsync(() -> {
//            try {
//                // 50명의 사용자 생성을 비동기로 처리
//                CompletableFuture<List<User>> usersFuture = CompletableFuture.supplyAsync(() -> {
//                    List<User> users = createUsersInBatch(50);
//                    log.info("50명의 사용자 생성 완료");
//                    return users;
//                }, taskExecutor);
//
//                // 사용자 생성이 완료된 후 팀 모집글과 프로젝트 모집글을 병렬로 생성
//                List<User> users = usersFuture.get();
//                CompletableFuture<List<TeamRecruit>> teamRecruitsFuture = CompletableFuture.supplyAsync(() -> {
//                    List<TeamRecruit> teamRecruits = createTeamRecruits(users, 50);
//                    log.info("50개의 팀 모집글 생성 완료");
//                    return teamRecruits;
//                }, taskExecutor);
//
//                CompletableFuture<List<ProjectRecruit>> projectRecruitsFuture = CompletableFuture.supplyAsync(() -> {
//                    List<ProjectRecruit> projectRecruits = createProjectRecruits(users, 50);
//                    log.info("50개의 프로젝트 모집글 생성 완료");
//                    return projectRecruits;
//                }, taskExecutor);
//
//                // 팀 모집글 생성이 완료된 후 북마크 생성
//                List<TeamRecruit> teamRecruits = teamRecruitsFuture.get();
//                CompletableFuture.runAsync(() -> {
//                    createBookmarks(users, teamRecruits, 50);
//                    log.info("50개의 북마크 생성 완료");
//                }, taskExecutor).get();
//
//                log.info("모든 테스트 데이터 생성이 완료되었습니다.");
//            } catch (Exception e) {
//                log.error("테스트 데이터 생성 중 오류 발생", e);
//            }
//        }, taskExecutor);
//    }
//
//    private void createUsersInBatch(int count) {
//        List<User> userBatch = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            userBatch.add(TestDataGenerator.createFakeUser());
//        }
//        userRepository.saveAll(userBatch);
//    }
//
//    private void createTeamRecruitsInBatch(List<User> users, int count) {
//        List<TeamRecruit> recruitBatch = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            User randomUser = users.get(random.nextInt(users.size()));
//            recruitBatch.add(TestDataGenerator.createFakeTeamRecruit(randomUser));
//        }
//        teamRecruitRepository.saveAll(recruitBatch);
//    }
//
//    private void createProjectRecruitsInBatch(List<User> users, int count) {
//        List<ProjectRecruit> recruitBatch = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            User randomUser = users.get(random.nextInt(users.size()));
//            recruitBatch.add(TestDataGenerator.createFakeProjectRecruit(randomUser));
//        }
//        projectRecruitRepository.saveAll(recruitBatch);
//    }
//
//    private void createBookmarksInBatch(List<User> users, List<TeamRecruit> teamRecruits, int count) {
//        List<TeamRecruitBookmark> bookmarkBatch = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            User randomUser = users.get(random.nextInt(users.size()));
//            TeamRecruit randomTeamRecruit = teamRecruits.get(random.nextInt(teamRecruits.size()));
//            bookmarkBatch.add(new TeamRecruitBookmark(randomUser, randomTeamRecruit));
//        }
//        bookmarkRepository.saveAll(bookmarkBatch);
//    }
//
//    private boolean isDataExists() {
//        return userRepository.count() > 0;
//    }
//}