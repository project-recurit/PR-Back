package com.example.sideproject.domain.pr.repository;

import com.example.sideproject.domain.pr.entity.Pr;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrRepository extends JpaRepository<Pr, Long> {
    @EntityGraph(value = "Pr.withTechStacks")
    Optional<Pr> findPrById(Long id);
}
