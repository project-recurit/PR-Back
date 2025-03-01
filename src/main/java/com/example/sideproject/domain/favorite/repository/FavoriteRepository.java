package com.example.sideproject.domain.favorite.repository;

import com.example.sideproject.domain.favorite.entity.Favorite;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByItemIdAndUser(Long itemId, User user);
}
