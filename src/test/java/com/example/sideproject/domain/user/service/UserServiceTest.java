package com.example.sideproject.domain.user.service;

import com.example.sideproject.domain.user.dto.ProfileResponseDto;
import com.example.sideproject.domain.user.dto.SignUpRequestDto;
import com.example.sideproject.domain.user.dto.SignUpResponseDto;
import com.example.sideproject.domain.user.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void registerSuccess() {
        // given
        Set<TechStack> techStacks = new HashSet<>();
        techStacks.add(TechStack.JAVA);

        SignUpRequestDto requestDto = SignUpRequestDto.builder()
                .username("testUser")
                .password("password123")
                .checkPassword("password123")
                .email("test@test.com")
                .nickname("nickname")
                .contact("010-1234-5678")
                .techStacks(techStacks)
                .build();

        given(userRepository.existsByUsername(any())).willReturn(false);
        given(userRepository.existsByNickname(any())).willReturn(false);

        // when
        SignUpResponseDto response = userService.register(requestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("중복된 사용자명으로 회원가입 실패 테스트")
    void registerFailDuplicateUsername() {
        // given
        SignUpRequestDto requestDto = SignUpRequestDto.builder()
                .username("testUser")
                .password("password123")
                .checkPassword("password123")
                .email("test@test.com")
                .nickname("nickname")
                .contact("010-1234-5678")
                .build();

        given(userRepository.existsByUsername("testUser")).willReturn(true);

        // when & then
        assertThrows(RuntimeException.class, () -> userService.register(requestDto));
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void withdrawUserTest() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .username("testUser")
                .password("password")
                .email("test@test.com")
                .nickname("nickname")
                .contact("contact")
                .techStacks(new HashSet<>())
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        userService.withdrawUser(userId);

        // then
        verify(userRepository).save(any(User.class));
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.INACTIVE_USER);
    }

    @Test
    @DisplayName("유저 상세 정보 조회 테스트")
    void findUserDetailTest() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .username("testUser")
                .password("password")
                .email("test@test.com")
                .nickname("nickname")
                .contact("contact")
                .techStacks(new HashSet<>())
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        ProfileResponseDto responseDto = userService.findUserDetail(userId);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getNickname()).isEqualTo("nickname");
    }

    @Test
    @DisplayName("기술 스택 업데이트 테스트")
    void updateUserStackTest() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .username("testUser")
                .password("password")
                .email("test@test.com")
                .nickname("nickname")
                .contact("contact")
                .techStacks(new HashSet<>())
                .build();

        Set<TechStack> newTechStacks = new HashSet<>();
        newTechStacks.add(TechStack.JAVA);
        newTechStacks.add(TechStack.SPRING);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        userService.updateUserStack(userId, newTechStacks);

        // then
        verify(userRepository).save(any(User.class));
        assertThat(user.getTechStacks()).containsAll(newTechStacks);
    }

    @Test
    @DisplayName("존재하지 않는 유저 조회 시 예외 발생 테스트")
    void findNonExistentUserTest() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> userService.findUserDetail(userId));
    }
}