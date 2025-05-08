package com.example.sideproject.domain.recruitment.repository;

import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentTechStackRepository extends JpaRepository<RecruitmentTechStack, Long> {
    void deleteByRecruitment(Recruitment recruitment);
}
