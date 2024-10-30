package com.example.sideproject.domain.user.repository;

import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByNickname(String nickname);

}
