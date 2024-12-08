package com.example.sideproject.domain.personal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sideproject.domain.personal.entity.ProjectRecruit;

@Repository
public interface ProjectRecruitRepository extends JpaRepository<ProjectRecruit, Long> {
    Optional<ProjectRecruit> findById(Long projectRecruitId);
} 