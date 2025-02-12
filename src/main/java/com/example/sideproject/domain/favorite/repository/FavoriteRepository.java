package com.example.sideproject.domain.favorite.repository;

import com.example.sideproject.domain.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository <Favorite, Long> {
}
