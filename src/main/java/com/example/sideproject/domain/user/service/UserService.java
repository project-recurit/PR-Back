package com.example.sideproject.domain.user.service;

import com.example.sideproject.domain.user.dto.ProfileRequestDto;
import com.example.sideproject.domain.user.dto.ProfileResponseDto;
import com.example.sideproject.domain.user.dto.SignUpRequestDto;
import com.example.sideproject.domain.user.dto.SignUpResponseDto;
import com.example.sideproject.domain.user.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        User user = new User(
            requestDto.getUsername(),
            passwordEncoder.encode(requestDto.getPassword()),
            requestDto.getEmail(),
            requestDto.getNickname(),
            requestDto.getContact(),
            requestDto.getTechStacks()
        );  

        return new SignUpResponseDto(userRepository.save(user));
    }

    private void validateSignUpRequest(SignUpRequestDto requestDto) {
        // 아이디 중복 검사
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new CustomException(ErrorType.DUPLICATE_ID);
        }

        // 닉네임 중복 검사
        if (userRepository.existsByNickname(requestDto.getNickname())) {
            throw new CustomException(ErrorType.DUPLICATE_NICKNAME);
        }

        // 비밀번호 확인
        if (!requestDto.getPassword().equals(requestDto.getCheckPassword())) {
            throw new CustomException(ErrorType.MISMATCH_PASSWORD);
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
    public void updateUserStack(String username, Set<TechStack> newTechStacks) {
        User user = findByUsername(username);
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

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
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
