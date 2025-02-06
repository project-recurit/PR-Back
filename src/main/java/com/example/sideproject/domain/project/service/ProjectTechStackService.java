package com.example.sideproject.domain.project.service;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.project.repository.ProjectTechStackRepository;
import com.example.sideproject.domain.techstack.entity.TechStack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectTechStackService {

    private final ProjectTechStackRepository projectTechStackRepository;

    public void createProjectTechStack(List<ProjectTechStack> projectTechStacks) {
        projectTechStackRepository.saveAll(projectTechStacks);
    }
}
