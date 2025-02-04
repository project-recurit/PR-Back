package com.example.sideproject.domain.project.repository;

import com.example.sideproject.domain.project.entity.ProjectTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {
}
