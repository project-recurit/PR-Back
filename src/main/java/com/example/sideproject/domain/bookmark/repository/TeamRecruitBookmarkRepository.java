package com.example.sideproject.domain.bookmark.repository;

import com.example.sideproject.domain.recruitment.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;
import com.example.sideproject.domain.user.entity.User;

@Repository
public interface TeamRecruitBookmarkRepository extends JpaRepository <TeamRecruitBookmark, Long> {
    boolean existsByUserAndRecruitment(User user, Recruitment recruitment);
    void deleteByUserAndRecruitment(User user, Recruitment recruitment);
    List<TeamRecruitBookmark> findAllByUserOrderByCreatedAtDesc(User user);
    Page<TeamRecruitBookmark> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
} 