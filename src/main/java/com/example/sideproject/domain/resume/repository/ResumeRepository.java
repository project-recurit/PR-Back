package com.example.sideproject.domain.resume.repository;

import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    boolean existsByUser(User user);

    @EntityGraph(attributePaths = {"experiences"})
    Optional<Resume> findResumeByUser(User user);

    @EntityGraph(attributePaths = {"experiences"})
    Optional<Resume> findResumeByIdAndUser(Long id, User user);

    Optional<Resume> findByUser(User user);
}
