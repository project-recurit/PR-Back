package com.example.sideproject.global.util;

import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.example.sideproject.domain.user.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.team.entity.TeamRecruit;
import com.example.sideproject.domain.personal.entity.ProjectRecruit;
import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;

import com.github.javafaker.Faker;

@SuppressWarnings("deprecation")
public class TestDataGenerator {
    private static final Faker faker = new Faker(new Locale("ko"));
    private static final Random random = new Random();

    public static User createFakeUser() {
        // 기본 필수 정보
        String username = faker.name().username();
        String password = generateSecurePassword();
        String email = faker.internet().emailAddress();
        String nickname = faker.name().firstName() + random.nextInt(1000);  // 중복 방지를 위한 랜덤 숫자 추가
        String contact = generateRandomContact();
        Set<TechStack> techStacks = generateRandomTechStacks();

        User user = new User(username, password, email, nickname, contact, techStacks);
        
        // 추가 정보 설정
        user.updateProfile(generateRandomProfileUrl());
        user.setLogin(); // 마지막 로그인 시간 설정
        
        // 경력 정보 추가
        Set<String> userHistory = generateRandomUserHistory();
        user.getUserHistory().addAll(userHistory);

        return user;
    }

    private static String generateSecurePassword() {
        return faker.internet().password(8, 20, true, true, true) + "!";  // 특수문자 포함
    }

    private static String generateRandomContact() {
        boolean isEmail = random.nextBoolean();
        if (isEmail) {
            return "email:" + faker.internet().emailAddress();
        } else {
            return "kakao:" + faker.name().username();
        }
    }

    private static Set<TechStack> generateRandomTechStacks() {
        Set<TechStack> techStacks = new HashSet<>();
        TechStack[] allTechStacks = TechStack.values();
        
        // 2~6개의 기술 스택 선택
        int numTechStacks = random.nextInt(5) + 2;
        while (techStacks.size() < numTechStacks) {
            techStacks.add(allTechStacks[random.nextInt(allTechStacks.length)]);
        }
        
        return techStacks;
    }

    private static String generateRandomProfileUrl() {
        String[] profileTypes = {
            "https://api.dicebear.com/7.x/avataaars/svg?seed=",
            "https://api.dicebear.com/7.x/bottts/svg?seed=",
            "https://api.dicebear.com/7.x/personas/svg?seed="
        };
        return profileTypes[random.nextInt(profileTypes.length)] + UUID.randomUUID().toString();
    }

    private static Set<String> generateRandomUserHistory() {
        Set<String> history = new HashSet<>();
        String[] companies = {
            "네이버", "카카오", "라인", "쿠팡", "배달의민족", "당근마켓", "토스",
            "우아한형제들", "야놀자", "직방", "마켓컬리"
        };
        String[] positions = {"백엔드 개발자", "프론트엔드 개발자", "풀스택 개발자", "DevOps 엔지니어"};
        String[] periods = {"1년", "2년", "3년", "6개월", "1년 6개월"};

        int historyCount = random.nextInt(3) + 1; // 1~3개의 경력 생성
        for (int i = 0; i < historyCount; i++) {
            String company = companies[random.nextInt(companies.length)];
            String position = positions[random.nextInt(positions.length)];
            String period = periods[random.nextInt(periods.length)];
            history.add(String.format("%s / %s / %s", company, position, period));
        }

        return history;
    }

    // TeamRecruit 더미 데이터 생성
    public static TeamRecruit createFakeTeamRecruit(User user) {
        return TeamRecruit.builder()
                .title(generateRecruitTitle())
                .content(generateRecruitContent())
                .techStacks(generateRandomTechStacks())
                .expectedPeriod(generateRandomPeriod())
                .fileUrl(generateRandomFileUrl())
                .contact(generateRandomContact())
                .user(user)
                .build();
    }

    // ProjectRecruit 더미 데이터 생성
    public static ProjectRecruit createFakeProjectRecruit(User user) {
        return new ProjectRecruit(
                generateRecruitTitle(),
                generateRecruitContent(),
                generateRandomTechStacks(),
                generateRandomPeriod(),
                generateRandomFileUrl(),
                generateRandomContact(),
                user
        );
    }

    // TeamRecruitBookmark 더미 데이터 생성
    public static TeamRecruitBookmark createFakeBookmark(User user, TeamRecruit teamRecruit) {
        return new TeamRecruitBookmark(user, teamRecruit);
    }

    // 공통으로 사용되는 헬퍼 메소드들
    private static String generateRecruitTitle() {
        String[] prefixes = {"[프로젝트]", "[스터디]", "[사이드]"};
        String[] subjects = {"웹 서비스", "앱 개발", "AI 프로젝트", "블록체인 개발", "클라우드 인프라"};
        String[] suffixes = {"팀원 모집", "같이 하실 분", "인원 충원"};
        
        return String.format("%s %s %s", 
            prefixes[random.nextInt(prefixes.length)],
            subjects[random.nextInt(subjects.length)],
            suffixes[random.nextInt(suffixes.length)]
        );
    }

    private static String generateRecruitContent() {
        StringBuilder content = new StringBuilder();
        content.append("프로젝트 소개:\n")
              .append(faker.lorem().paragraph())
              .append("\n\n모집 인원:\n")
              .append("- 백엔드: ").append(random.nextInt(3) + 1).append("명\n")
              .append("- 프론트엔드: ").append(random.nextInt(3) + 1).append("명\n\n")
              .append("자격 요건:\n")
              .append(faker.lorem().paragraph());
        return content.toString();
    }

    private static String generateRandomPeriod() {
        String[] periods = {"1개월", "2개월", "3개월", "6개월", "1년"};
        return periods[random.nextInt(periods.length)];
    }

    private static String generateRandomFileUrl() {
        if (random.nextBoolean()) { // 50% 확률로 파일 URL 생성
            return "https://example.com/files/" + UUID.randomUUID().toString() + ".pdf";
        }
        return null;
    }
} 