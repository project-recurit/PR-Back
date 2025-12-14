package com.example.sideproject.domain.techstack.service.impl;

import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.repository.TechStackCacheRepository;
import com.example.sideproject.domain.techstack.service.CacheTechStackStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisTechStackStorage implements CacheTechStackStorage {
    private final TechStackCacheRepository techStackCacheRepository;

    @Override
    public List<TechStack> findAll() {
        try {
            return techStackCacheRepository.findTechStack();
        } catch (Exception e) {
            log.warn("Failed to fetch from cache", e);
            return List.of();
        }
    }

    @Override
    public void saveAll(List<TechStack> techStacks) {
        try {
            techStacks.forEach(techStackCacheRepository::save);
        } catch (Exception e) {
            log.warn("Failed to save to cache", e);
        }
    }
}