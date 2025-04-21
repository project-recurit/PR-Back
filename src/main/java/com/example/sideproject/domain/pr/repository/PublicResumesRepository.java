package com.example.sideproject.domain.pr.repository;

import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicResumesRepository extends JpaRepository<Pr, Long> {
//    Optional<Pr> findByResumeAndUser(Resume resume, User user);
//    boolean existsByResume(Resume resume);
}
