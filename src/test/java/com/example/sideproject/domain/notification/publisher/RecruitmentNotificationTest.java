package com.example.sideproject.domain.notification.publisher;

import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import com.example.sideproject.domain.notification.entity.NotificationType;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserTechStack;
import com.example.sideproject.fake.FakeApplicationEventPublisher;
import com.example.sideproject.global.component.ObjectConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecruitmentNotificationTest {
    RecruitmentNotification recruitmentNotification;
    FakeApplicationEventPublisher fakePublisher;

    @BeforeEach
    void setUp() {
        fakePublisher = new FakeApplicationEventPublisher();
        ObjectConverter objectConverter = new ObjectConverter();

        recruitmentNotification = new RecruitmentNotification(fakePublisher, objectConverter);
    }

    @DisplayName("프로젝트 등록 시 매칭되는 기술스택을 가진 유저들에게만 알림이 발송된다")
    @Test
    void notice() {
        // given
        Long projectId = 1L;
        Long leaderId = 100L;
        Recruitment recruitment = getRecruitment(projectId, leaderId);
        List<User> users = getUsers();
        List<Long> recruitmentTechStackIds = List.of(1L, 2L); // React, Spring만 매칭

        // when
        recruitmentNotification.notice(recruitment, users, recruitmentTechStackIds);

        // then
        EventListDto publishedEvents = fakePublisher.getLastEvent();
        List<EventDto> eventDtos = publishedEvents.eventDtos();

        assertThat(eventDtos).hasSize(3); // 3명의 유저

        // 각 유저별로 매칭되는 기술스택 검증
        EventDto user2Event = eventDtos.stream()
                .filter(event -> event.to().equals(2L))
                .findFirst().orElseThrow();

        EventDto user3Event = eventDtos.stream()
                .filter(event -> event.to().equals(3L))
                .findFirst().orElseThrow();

        EventDto user4Event = eventDtos.stream()
                .filter(event -> event.to().equals(4L))
                .findFirst().orElseThrow();

        // 공통 필드 검증
        assertThat(user2Event.from()).isEqualTo(leaderId);
        assertThat(user2Event.relatedId()).isEqualTo(projectId);
        assertThat(user2Event.type()).isEqualTo(NotificationType.PROJECT_REGISTRATION);

        // 메시지에 매칭된 기술스택이 포함되어 있는지 검증
        // User2: React, Spring 둘 다 매칭
        assertThat(user2Event.msg()).contains("React", "Spring");

        // User3: React만 매칭
        assertThat(user3Event.msg()).contains("React");
        assertThat(user3Event.msg()).doesNotContain("Vue");

        // User4: Spring만 매칭
        assertThat(user4Event.msg()).contains("Spring");
        assertThat(user4Event.msg()).doesNotContain("Django");
    }

    @DisplayName("매칭되는 기술스택이 없는 유저는 알림이 발송되지 않는다")
    @Test
    void notice_NoMatchingTechStacks() {
        // given
        Long projectId = 1L;
        Long leaderId = 100L;
        Recruitment recruitment = getRecruitment(projectId, leaderId);
        List<User> users = getUsers();
        List<Long> recruitmentTechStackIds = List.of(99L); // 매칭되지 않는 기술스택

        // when
        recruitmentNotification.notice(recruitment, users, recruitmentTechStackIds);

        // then
        EventListDto publishedEvents = fakePublisher.getLastEvent();
        List<EventDto> eventDtos = publishedEvents.eventDtos();

        assertThat(eventDtos).hasSize(0);
    }

    Recruitment getRecruitment(Long projectId, Long userId) {
        return Recruitment.builder()
                .id(projectId)
                .title("프로젝트 하실분")
                .user(User.builder().userId(userId).build())
                .build();
    }

    List<User> getUsers() {
        // 기술 스택 생성
        TechStack react = TechStack.builder().id(1L).name("React").build();
        TechStack spring = TechStack.builder().id(2L).name("Spring").build();
        TechStack vue = TechStack.builder().id(3L).name("Vue").build();
        TechStack django = TechStack.builder().id(4L).name("Django").build();

        // User 1: React, Spring
        User user1 = User.builder().userId(2L).build();
        List<UserTechStack> userTechStacks1 = List.of(
                UserTechStack.builder().techStack(react).build(),
                UserTechStack.builder().techStack(spring).build()
        );
        ReflectionTestUtils.setField(user1, "userTechStacks", userTechStacks1);

        // User 2: React, Vue
        User user2 = User.builder().userId(3L).build();
        List<UserTechStack> userTechStacks2 = List.of(
                UserTechStack.builder().techStack(react).build(),
                UserTechStack.builder().techStack(vue).build()
        );
        ReflectionTestUtils.setField(user2, "userTechStacks", userTechStacks2);

        // User 3: Spring, Django
        User user3 = User.builder().userId(4L).build();
        List<UserTechStack> userTechStacks3 = List.of(
                UserTechStack.builder().techStack(spring).build(),
                UserTechStack.builder().techStack(django).build()
        );
        ReflectionTestUtils.setField(user3, "userTechStacks", userTechStacks3);

        return List.of(user1, user2, user3);
    }
}