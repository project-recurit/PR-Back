package com.example.sideproject.domain.recruitment.repository;

import com.example.sideproject.domain.recruitment.entity.RecruitmentTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentTechStackRepository extends JpaRepository<RecruitmentTechStack, Long> {
    List<RecruitmentTechStack> findAllByRecruitmentId(Long recruitmentId);
    void deleteAllByRecruitmentId(Long recruitmentId);
}
