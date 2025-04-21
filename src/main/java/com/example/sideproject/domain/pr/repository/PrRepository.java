package com.example.sideproject.domain.pr.repository;

import com.example.sideproject.domain.pr.entity.Pr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrRepository extends JpaRepository<Pr, Long> {
//    Optional<Pr> findByResumeAndUser(Resume resume, User user);
//    boolean existsByResume(Resume resume);
}
