package com.example.sideproject.domain.project.repository;

import com.example.sideproject.domain.project.entity.ProjectUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUrlRepository extends JpaRepository<ProjectUrl, Long> {
}
