package com.example.sideproject.domain.recruitment.repository;

import com.example.sideproject.domain.recruitment.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    @Query("SELECT DISTINCT p FROM Recruitment p " +
            "LEFT JOIN FETCH p.recruitmentTechStacks pts " +
            "LEFT JOIN FETCH pts.techStack")
    List<Recruitment> findAllWithTechStacks();
}
