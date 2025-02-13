package com.example.sideproject.domain.project.service;

import com.example.sideproject.domain.project.dto.ProjectTechStackResponseDto;
import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.project.repository.ProjectTechStackRepository;
import com.example.sideproject.domain.project.repository.query.ProjectTechStackQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectTechStackService {

    private final ProjectTechStackRepository projectTechStackRepository;
    private final ProjectTechStackQueryRepository queryRepository;
    public void createProjectTechStack(List<ProjectTechStack> projectTechStacks) {
        projectTechStackRepository.saveAll(projectTechStacks);
    }

    public List<ProjectTechStackResponseDto> getProjectTechStacks(Long projectId) {
        return queryRepository.getProjectTechStacks(projectId);
    }
}
