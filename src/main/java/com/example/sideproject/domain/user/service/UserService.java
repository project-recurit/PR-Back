package com.example.sideproject.domain.user.service;

import com.example.sideproject.domain.user.dto.ProfileRequestDto;
import com.example.sideproject.domain.user.dto.ProfileResponseDto;
import com.example.sideproject.domain.user.dto.SignUpRequestDto;
import com.example.sideproject.domain.user.dto.SignUpResponseDto;
import com.example.sideproject.domain.user.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    /**
     * 회원가입
     * @param requestDto
     * @return SingResponseDto
     */
    @Transactional
    public SignUpResponseDto register(SignUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String nickname = requestDto.getNickname();
        String checkPassword = requestDto.getCheckPassword();
        String email = requestDto.getEmail();
        String contact = requestDto.getContact();

        if(userRepository.existsByUsername(username)){
            throw new RuntimeException("이미 있는 ID입니다.");
        }

        if(userRepository.existsByNickname(nickname)){
            throw new RuntimeException("이미 있는 닉네임 입니다.");
        }
        if(!password.equals(checkPassword)){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

       User user = new User(
               username,
               password,
               email,
               nickname,
               contact,
               requestDto.getTechStacks()
       );
       userRepository.save(user);

        return new SignUpResponseDto(user);
    }

    /**
     * 유저-회원탈퇴(소프트 리셋)
     * @param userId
     */
    @Transactional
    public void withdrawUser(Long userId) {
        User user = findByid(userId);
        user.withDraw();
        userRepository.save(user);
    }

    /**
     * 유저 프로필 상세 조회-> 마이페이지
     * @param userId
     * @return ProfileResponseDto
     */
    public ProfileResponseDto findUserDetail(Long userId) {
        return new ProfileResponseDto(findByid(userId));
    }

    /**
     * 유저 상세정보 - 기술 스택 추가
     * @param username
     * @param newTechStacks
     */
    @Transactional
    public void updateUserStack(String username,Set<TechStack> newTechStacks) {
        User user = findUser(username);
        user.addTechStack(newTechStacks);
        userRepository.save(user);
    }

    /**
     * 유저 조회
     * @param username
     * @return User
     */
    private User findUser(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 유저 정보 수정
     * @param userId
     * @param requestDto
     */
    public void updateUserDetails(Long userId, ProfileRequestDto requestDto) {

    }
    
    private User findByid(Long id){
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
    }


}
