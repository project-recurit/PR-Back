package com.example.sideproject.domain.applicant.repository;

import com.example.sideproject.domain.applicant.entity.Applicant;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    Optional<Applicant> findByIdAndProjectAndUser(Long id, Project project, User user);

    boolean existsByProjectAndUser(Project project, User user);
}
