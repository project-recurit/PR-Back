package com.example.sideproject.domain.recruitment.repository;

import com.example.sideproject.domain.recruitment.entity.RecruitmentPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentPositionRepository extends JpaRepository<RecruitmentPosition, Long> {
}
