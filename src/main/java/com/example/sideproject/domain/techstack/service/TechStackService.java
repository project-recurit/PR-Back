package com.example.sideproject.domain.techstack.service;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechStackService {

    private final TechStackRepository techStackRepository;

    public List<TechStackDto> getTeckStackList(){
        List<TechStack> techStacks = techStackRepository.findAll();
        return TechStackDto.of(techStacks);
    }



}
