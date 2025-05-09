package com.example.sideproject.domain.recruitment.repository;

import com.example.sideproject.domain.recruitment.entity.RecruitmentFavorite;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentFavoriteRepository extends JpaRepository<RecruitmentFavorite, Long> {
    boolean existsByUserAndRecruitment_Id(User user, Long prId);
}
