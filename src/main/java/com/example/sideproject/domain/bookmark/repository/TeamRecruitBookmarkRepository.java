package com.example.sideproject.domain.bookmark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.project.entity.Project;

@Repository
public interface TeamRecruitBookmarkRepository extends JpaRepository <TeamRecruitBookmark, Long> {
    boolean existsByUserAndProject(User user, Project project);
    void deleteByUserAndProject(User user, Project project);
    List<TeamRecruitBookmark> findAllByUserOrderByCreatedAtDesc(User user);
    Page<TeamRecruitBookmark> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
} 