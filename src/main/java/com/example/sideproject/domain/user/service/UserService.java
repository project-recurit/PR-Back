package com.example.sideproject.domain.user.service;

import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.dto.*;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.entity.UserTechStack;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.auth.service.TokenService;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Transactional
    public void register(SignUpRequestDto requestDto) {
        User user = requestDto.toEntity(UserStatus.INACTIVE_USER);

        userRepository.save(user);
    }

    @Transactional
    //todo 업데이트 한번에
    public UpdateRegisterResponseDto updateRegister(UpdateRegisterRequestDto requestDto) {
        // 소셜 ID로 유저 찾기
        User user = userRepository.findBySocialId(requestDto.socialId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

        validateSignUpRequest(requestDto.nickname());

        // 기술 스택 엔티티 조회
        List<TechStack> techStacks = requestDto.techStackIds().stream()
                .map(id -> TechStack.builder()
                        .id(id)
                        .build())
                .toList();

        // 유저 정보 업데이트
        user.updateRegisterInfo(
                requestDto.position(),
                requestDto.nickname(),
                techStacks
        );

        User savedUser = userRepository.save(user);
        String accessToken = tokenService.createAccessToken(
                savedUser.getSocialId(),
                savedUser.getUserStatus(),
                savedUser.getUserRole()
        );
        String refreshToken = tokenService.createRefreshToken();

        // 리프레시 토큰 저장
        savedUser.updateRefreshToken(refreshToken);
        return UpdateRegisterResponseDto.ofUpdateRegister(savedUser,accessToken,refreshToken);
    }

    private void validateSignUpRequest(String nickname) {
        // 닉네임 중복 검사
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorType.DUPLICATE_NICKNAME);
        }
    }

    @Transactional
    public void withdrawUser(Long userId) {
        User user = findActiveUser(userId);
        user.withDraw();
        userRepository.save(user);
    }

    public ProfileResponseDto findUserDetail(Long userId) {
        User user = findActiveUser(userId);
        return new ProfileResponseDto(user);
    }

    @Transactional
    public void updateUserStack(String socialId, List<UserTechStack> newTechStacks) {
        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        validateActiveUser(user);
        
        user.updateTechStacks(newTechStacks);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserDetails(Long userId, ProfileRequestDto requestDto) {
        User user = findActiveUser(userId);
        
        // 닉네임 변경 시 중복 체크
        if (!user.getNickname().equals(requestDto.getNickname()) 
                && userRepository.existsByNickname(requestDto.getNickname())) {
            throw new CustomException(ErrorType.DUPLICATE_NICKNAME);
        }

        user.updateProfile(
                requestDto.getNickname()
        );
        
        userRepository.save(user);
    }

    private User findBySocialId(String username) {
        return userRepository.findBySocialId(username)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
    }

    private User findActiveUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        
        validateActiveUser(user);
        return user;
    }

    private void validateActiveUser(User user) {
        if (!user.isActive()) {
            throw new CustomException(ErrorType.WITHDRAW_USER);
        }
    }
}
