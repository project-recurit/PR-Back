package com.example.sideproject.domain.user.service;

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


    public SignUpResponseDto register(SignUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String nickname = requestDto.getNickname();
        String checkPassword = requestDto.getCheckPassword();
        String email = requestDto.getEmail();
        String phone = requestDto.getPhone();

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
               password,
               email,
               nickname,
               phone,
               requestDto.getTechStacks()
       );

        return new SignUpResponseDto(user);
    }

    @Transactional
    public void withdrawUser(Long userId) {
        User user = findUser(userId);
        user.withDraw();
        userRepository.save(user);
    }

    public void findUserDetail(Long userId) {

    }

    @Transactional
    public void updateUserStack(Long userId, Set<TechStack> newTechStacks) {
        User user =  findUser(userId);
        user.addTechStack(newTechStacks);
        userRepository.save(user);
    }

    //유저 조회
    private User findUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}
