package com.example.sideproject.global.security;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String socialId) throws UsernameNotFoundException {
        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        if (user.getUserStatus() == UserStatus.WITHDRAW_USER) {
            throw new CustomException(ErrorType.WITHDRAW_USER);
        }

        return new UserDetailsImpl(user);
    }
}
