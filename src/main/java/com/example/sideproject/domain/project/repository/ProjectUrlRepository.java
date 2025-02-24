package com.example.sideproject.domain.project.repository;

import com.example.sideproject.domain.project.entity.ProjectUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectUrlRepository extends JpaRepository<ProjectUrl, Long> {
    List<ProjectUrl> findAllByProjectId(Long projectId);
}
