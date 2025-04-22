package com.example.sideproject.domain.pr.repository;

import com.example.sideproject.domain.pr.entity.PrComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrCommentRepository extends JpaRepository<PrComment, Long> {
}
