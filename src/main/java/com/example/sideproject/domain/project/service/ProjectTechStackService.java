package com.example.sideproject.domain.project.service;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.project.repository.ProjectTechStackRepository;
import com.example.sideproject.domain.techstack.TechStack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectTechStackService {

    private final ProjectTechStackRepository projectTechStackRepository;

    public void createProjectTechStack(TechStack techStack, Project project) {
        ProjectTechStack projectTechStack = ProjectTechStack.builder()
                .techStack(techStack)
                .project(project)
                .build();
        projectTechStackRepository.save(projectTechStack);
    }
}
