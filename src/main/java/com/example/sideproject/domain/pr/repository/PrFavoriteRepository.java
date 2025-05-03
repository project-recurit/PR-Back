package com.example.sideproject.domain.pr.repository;

import com.example.sideproject.domain.pr.entity.PrFavorite;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrFavoriteRepository extends JpaRepository<PrFavorite, Long> {
    boolean existsByUserAndPr_Id(User user, Long prId);
}
