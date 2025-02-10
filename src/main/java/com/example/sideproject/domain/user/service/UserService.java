package com.example.sideproject.domain.user.service;

import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.dto.ProfileRequestDto;
import com.example.sideproject.domain.user.dto.ProfileResponseDto;
import com.example.sideproject.domain.user.dto.SignUpRequestDto;
import com.example.sideproject.domain.user.dto.SignUpResponseDto;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserTechStack;
import com.example.sideproject.domain.user.repository.UserRepository;
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
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponseDto register(SignUpRequestDto requestDto) {
        validateSignUpRequest(requestDto);

       User user = requestDto.toEntity();

        userRepository.save(user);

        return new SignUpResponseDto(user);
    }

    private void validateSignUpRequest(SignUpRequestDto requestDto) {
        // 닉네임 중복 검사
        if (userRepository.existsByNickname(requestDto.nickname())) {
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
