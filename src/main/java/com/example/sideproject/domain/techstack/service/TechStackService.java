package com.example.sideproject.domain.techstack.service;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.repository.TechStackCacheRepository;
import com.example.sideproject.domain.techstack.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechStackService {
    private final TechStackRepository techStackRepository;
    private final TechStackCacheRepository techStackCacheRepository;

    public List<TechStackDto> getTeckStackList(){
        List<TechStack> cacheTechStacks = techStackCacheRepository.findTechStack();

        if (!cacheTechStacks.isEmpty()) {
            return TechStackDto.of(cacheTechStacks);
        }

        List<TechStack> techStacks = techStackRepository.findAll();
        for (TechStack techStack : techStacks) {
            techStackCacheRepository.save(techStack);
        }
        return TechStackDto.of(techStacks);
    }

}
