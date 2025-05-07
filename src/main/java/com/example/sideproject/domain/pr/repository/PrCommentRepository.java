package com.example.sideproject.domain.pr.repository;

import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.PrComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrCommentRepository extends JpaRepository<PrComment, Long> {
    @EntityGraph(attributePaths = {"user"})
    Page<PrComment> findByPr_IdAndParentIsNullOrderById(Long prId, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    List<PrComment> findByParent_IdOrderById(Long parentId, Pageable pageable);

}
