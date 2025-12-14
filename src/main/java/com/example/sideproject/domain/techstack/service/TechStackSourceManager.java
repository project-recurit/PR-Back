package com.example.sideproject.domain.techstack.service;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.entity.TechStack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TechStackSourceManager {
    private final CacheTechStackStorage cacheStorage;
    private final PersistentTechStackStorage databaseStorage;

    public List<TechStackDto> getTechStackList() {
        // 1. 캐시에서 조회
        List<TechStack> cached = cacheStorage.findAll();
        
        if (!cached.isEmpty()) {
            return TechStackDto.of(cached);
        }
        
        // 2. 캐시 미스 - DB 조회
        log.info("Cache miss, fetching from database");
        List<TechStack> fromDb = databaseStorage.findAll();
        
        // 3. 캐시에 저장
        cacheStorage.saveAll(fromDb);
        
        return TechStackDto.of(fromDb);
    }
}