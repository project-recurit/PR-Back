package com.example.sideproject.domain.user.repository;

import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByNickname(String nickname);

    Optional<User> findByUsername(String username);


    Optional<User> findByRefreshToken(String refreshToken);
}
