package com.example.sideproject.domain.techstack.service;

import com.example.sideproject.domain.techstack.entity.TechStack;

import java.util.List;

public interface CacheTechStackStorage {
    List<TechStack> findAll();
    void saveAll(List<TechStack> techStacks);
}
