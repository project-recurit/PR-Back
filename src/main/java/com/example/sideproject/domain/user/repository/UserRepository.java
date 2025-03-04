package com.example.sideproject.domain.user.repository;

import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByNickname(String nickname);

    Optional<User> findBySocialId(String socialId);

    Optional<User> findByRefreshToken(String refreshToken);

    @EntityGraph(attributePaths = {"userTechStacks"})
    List<User> findByUserTechStacks_TechStackIn(List<TechStack> techStacks);

    boolean existsBySocialId(String socialId);
}
