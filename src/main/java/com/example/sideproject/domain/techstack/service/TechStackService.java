package com.example.sideproject.domain.techstack.service;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.repository.TechStackCacheRepository;
import com.example.sideproject.domain.techstack.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechStackService {
    private final TechStackSourceManager techStackSourceManager;

    public List<TechStackDto> getTeckStackList(){
        return techStackSourceManager.getTechStackList();
    }

}
