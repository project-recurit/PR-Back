package com.example.sideproject.domain.pr.repository;

import com.example.sideproject.domain.pr.entity.PublicResumes;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicResumesRepository extends JpaRepository<PublicResumes, Long> {
    Optional<PublicResumes> findByResumeAndUser(Resume resume, User user);
    boolean existsByResume(Resume resume);
}
