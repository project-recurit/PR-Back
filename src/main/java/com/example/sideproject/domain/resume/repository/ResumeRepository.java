package com.example.sideproject.domain.resume.repository;

import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    boolean existsByUser(User user);

    @EntityGraph(attributePaths = {"experiences"})
    Optional<Resume> findResumeByUser(User user);

    @EntityGraph(attributePaths = {"experiences"})
    Optional<Resume> findResumeByIdAndUser(Long id, User user);

    @EntityGraph(attributePaths = {"experiences"})
    Optional<Resume> findResumeById(Long id);

    Optional<Resume> findByUser(User user);

    Page<Resume> findByPublishedAtIsNotNull(Pageable pageable);
}
