package com.example.sideproject.domain.team.repository;

import com.example.sideproject.domain.team.entity.TeamRecruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRecruitRepository extends JpaRepository<TeamRecruit, Long> {
    Optional<TeamRecruit> findById(Long teamRecruitId);
}
