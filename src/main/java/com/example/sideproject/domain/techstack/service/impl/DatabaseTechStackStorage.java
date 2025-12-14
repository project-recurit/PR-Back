package com.example.sideproject.domain.techstack.service.impl;

import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.repository.TechStackRepository;
import com.example.sideproject.domain.techstack.service.PersistentTechStackStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseTechStackStorage implements PersistentTechStackStorage {
    private final TechStackRepository techStackRepository;

    @Override
    public List<TechStack> findAll() {
        return techStackRepository.findAll();
    }

    @Override
    public void saveAll(List<TechStack> techStacks) {
        techStackRepository.saveAll(techStacks);
    }
}