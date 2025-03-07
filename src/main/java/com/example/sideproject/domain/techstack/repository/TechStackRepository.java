package com.example.sideproject.domain.techstack.repository;

import com.example.sideproject.domain.techstack.entity.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
}
