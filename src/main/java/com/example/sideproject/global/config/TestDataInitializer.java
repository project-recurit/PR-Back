package com.example.sideproject.global.config;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.team.entity.TeamRecruit;
import com.example.sideproject.domain.personal.entity.ProjectRecruit;
import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.domain.team.repository.TeamRecruitRepository;
import com.example.sideproject.domain.personal.repository.ProjectRecruitRepository;
import com.example.sideproject.domain.bookmark.repository.TeamRecruitBookmarkRepository;
import com.example.sideproject.global.util.TestDataGenerator;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("local")
public class TestDataInitializer {
    private final UserRepository userRepository;
    private final TeamRecruitRepository teamRecruitRepository;
    private final ProjectRecruitRepository projectRecruitRepository;
    private final TeamRecruitBookmarkRepository bookmarkRepository;
    private final Random random = new Random();

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeTestData() {
        if (isDataExists()) {
            log.info("테스트 데이터가 이미 존재합니다.");
            return;
        }

        // 50명의 사용자 생성
        List<User> users = createUsers(50);
        log.info("50명의 사용자 생성 완료");

        // 50개의 팀 모집글 생성
        List<TeamRecruit> teamRecruits = createTeamRecruits(users, 50);
        log.info("50개의 팀 모집글 생성 완료");

        // 50개의 프로젝트 모집글 생성
        List<ProjectRecruit> projectRecruits = createProjectRecruits(users, 50);
        log.info("50개의 프로젝트 모집글 생성 완료");

        // 50개의 북마크 생성
        createBookmarks(users, teamRecruits, 50);
        log.info("50개의 북마크 생성 완료");

        log.info("모든 테스트 데이터 생성이 완료되었습니다.");
    }

    private boolean isDataExists() {
        return userRepository.count() > 0;
    }

    private List<User> createUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = TestDataGenerator.createFakeUser();
            users.add(userRepository.save(user));
        }
        return users;
    }

    private List<TeamRecruit> createTeamRecruits(List<User> users, int count) {
        List<TeamRecruit> teamRecruits = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User randomUser = users.get(random.nextInt(users.size()));
            TeamRecruit teamRecruit = TestDataGenerator.createFakeTeamRecruit(randomUser);
            teamRecruits.add(teamRecruitRepository.save(teamRecruit));
        }
        return teamRecruits;
    }

    private List<ProjectRecruit> createProjectRecruits(List<User> users, int count) {
        List<ProjectRecruit> projectRecruits = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User randomUser = users.get(random.nextInt(users.size()));
            ProjectRecruit projectRecruit = TestDataGenerator.createFakeProjectRecruit(randomUser);
            projectRecruits.add(projectRecruitRepository.save(projectRecruit));
        }
        return projectRecruits;
    }

    private void createBookmarks(List<User> users, List<TeamRecruit> teamRecruits, int count) {
        for (int i = 0; i < count; i++) {
            User randomUser = users.get(random.nextInt(users.size()));
            TeamRecruit randomTeamRecruit = teamRecruits.get(random.nextInt(teamRecruits.size()));
            TeamRecruitBookmark bookmark = TestDataGenerator.createFakeBookmark(randomUser, randomTeamRecruit);
            bookmarkRepository.save(bookmark);
        }
    }
} 