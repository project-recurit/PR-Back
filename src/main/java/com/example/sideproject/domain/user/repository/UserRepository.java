package com.example.sideproject.domain.user.repository;

import com.example.sideproject.domain.user.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByNickname(String nickname);

    Optional<User> findBySocialId(String socialId);

    Optional<User> findByRefreshToken(String refreshToken);

    List<User> findByTechStacksIn(Set<TechStack> techStacks);

    boolean existsBySocialId(String socialId);
}
