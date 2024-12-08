package com.example.sideproject.domain.bookmark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.team.entity.TeamRecruit;

@Repository
public interface TeamRecruitBookmarkRepository extends JpaRepository <TeamRecruitBookmark, Long> {
    boolean existsByUserAndTeamRecruit(User user, TeamRecruit teamRecruit);
    void deleteByUserAndTeamRecruit(User user, TeamRecruit teamRecruit);
    List<TeamRecruitBookmark> findAllByUserOrderByCreatedAtDesc(User user);
    Page<TeamRecruitBookmark> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
} 