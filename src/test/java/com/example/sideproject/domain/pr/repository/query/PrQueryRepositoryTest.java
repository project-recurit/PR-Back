package com.example.sideproject.domain.pr.repository.query;

import com.example.RepositoryTest;
import com.example.sideproject.domain.pr.dto.PrListResponseDto;
import com.example.sideproject.domain.pr.dto.PrSearchRequest;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.PrExperience;
import com.example.sideproject.domain.pr.entity.PrTechStack;
import com.example.sideproject.domain.pr.repository.PrRepository;
import com.example.sideproject.domain.recruitment.entity.RecruitmentTechStack;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.dto.DateSort;
import com.example.sideproject.global.dto.PageDto;
import com.example.sideproject.global.dto.SearchDto;
import com.example.sideproject.global.entity.PostCount;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PrQueryRepository.class, TechStackQueryRepository.class})
@Transactional
class PrQueryRepositoryTest extends RepositoryTest {
    @Autowired
    PrQueryRepository prQueryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PrRepository prRepository;

    Pr pr;

    @BeforeEach
    void setUp() {
        TechStack techStack1 = new TechStack(1L, "Spring");
        TechStack techStack2 = new TechStack(2L, "Spring AI");

        User user = User.builder()
                .userId(1L)
                .nickname("user1")
                .email("user1@test.com")
                .socialId("1")
                .socialProvider("KAKAO")
                .username("test_user1")
                .userStatus(UserStatus.ACTIVE_USER)
                .build();

        user = userRepository.save(user);

        System.out.println("======== 유저 입력");

        pr = Pr.builder()
                .user(user)
                .count(new PostCount(1,2,3))
                .documentUrl(List.of("url1", "url2", "url3"))
                .title("pr1Title")
                .introduce("pr1Introduce")
                .position(Position.FRONTEND)
                .workType(WorkType.ALL)
                .techStacks(getTechStacks(techStack1, techStack2))
                .experiences(List.of(
                        PrExperience.builder()
                                .description("exp1_desc")
                                .teamSize(5)
                                .startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now())
                                .title("exp1")
                                .documentUrl("url")
                                .build()
                ))
                .build();

        Pr pr2 = Pr.builder()
                .user(user)
                .count(new PostCount(1,2,3))
                .documentUrl(List.of("url1", "url2", "url3"))
                .title("pr2Title")
                .introduce("pr2Introduce")
                .position(Position.BACKEND)
                .workType(WorkType.ALL)
                .techStacks(getTechStacks(techStack1, techStack2))
                .experiences(List.of(
                        PrExperience.builder()
                                .description("exp1_desc")
                                .teamSize(3)
                                .startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now())
                                .title("exp1")
                                .documentUrl("url")
                                .build()
                ))
                .build();

        pr = prRepository.save(pr);
        prRepository.save(pr2);

        System.out.println("========== pr 작성 완료");
    }

    List<PrTechStack> getTechStacks(TechStack techStack1, TechStack techStack2) {
        return List.of(new PrTechStack(null, techStack1, 10),
                       new PrTechStack(null, techStack2, 10));
    }

    @DisplayName("pr 목록을 조회한다")
    @Test
    void getPrs() {
        // given
        PrSearchRequest prSearchRequest = new PrSearchRequest(
                SearchDto.create(),
                List.of(Position.FRONTEND),
                List.of(1L),
                List.of(WorkType.ALL),
                null
        );

        DateSort dateSort = prSearchRequest.searchDto().dateSort();
        PageDto pageDto = prSearchRequest.searchDto().pageDto();

        Sort sort = Sort.by(Sort.Order.desc(dateSort.getOrder()));
        PageRequest pageRequest = pageDto.toPageRequest(sort);

        // when
        Page<PrListResponseDto> prs = prQueryRepository.getPrs(pageRequest, prSearchRequest);
        PagedModel<PrListResponseDto> result = new PagedModel<>(prs);

        // then
        assertThat(prs.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTechStacks()).isNotNull();
    }
}