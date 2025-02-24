package com.example.sideproject.domain.project.repository;

import com.example.sideproject.domain.project.entity.ProjectTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {
    List<ProjectTechStack> findAllByProjectId(Long projectId);
    void deleteAllByProjectId(Long projectId);
}
