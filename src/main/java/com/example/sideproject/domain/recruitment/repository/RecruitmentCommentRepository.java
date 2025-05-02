package com.example.sideproject.domain.recruitment.repository;

import com.example.sideproject.domain.recruitment.entity.RecruitmentComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentCommentRepository extends JpaRepository<RecruitmentComment, Long> {
}
