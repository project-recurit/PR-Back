package com.example.sideproject.domain.user.repository;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserTechStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByNickname(String nickname);

    Optional<User> findBySocialId(String socialId);

    Optional<User> findByRefreshToken(String refreshToken);

    List<User> findByTechStack1sIn(List<UserTechStack> userTechStacks);

    boolean existsBySocialId(String socialId);
}
