package com.example.sideproject.domain.applicant.repository;

import com.example.sideproject.domain.applicant.entity.Applicant;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    Optional<Applicant> findByIdAndRecruitment(Long id, Recruitment recruitment);
    boolean existsByRecruitmentAndUser(Recruitment recruitment, User user);
}
