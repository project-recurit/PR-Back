package com.example.sideproject.domain.pr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.sideproject.domain.pr.entity.PublicRelation;

@Repository
public interface PublicRelationRepository extends JpaRepository<PublicRelation, Long> {
    Optional<PublicRelation> findById(Long projectRecruitId);

    Page<PublicRelation> findAll(Pageable pageable);
} 