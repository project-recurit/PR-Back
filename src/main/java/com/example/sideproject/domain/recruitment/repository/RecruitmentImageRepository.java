package com.example.sideproject.domain.recruitment.repository;

import com.example.sideproject.domain.recruitment.entity.RecruitmentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentImageRepository extends JpaRepository<RecruitmentImage, Long> {
    List<RecruitmentImage> findAllByRecruitmentId(Long recruitmentId);
}
