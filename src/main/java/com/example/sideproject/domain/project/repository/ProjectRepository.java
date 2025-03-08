package com.example.sideproject.domain.project.repository;

import com.example.sideproject.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN FETCH p.projectTechStacks pts " +
            "LEFT JOIN FETCH pts.techStack")
    List<Project> findAllWithTechStacks();
}
