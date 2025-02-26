package com.example.sideproject.domain.favorite.controller;

import com.example.sideproject.domain.favorite.service.FavoriteService;
import com.example.sideproject.domain.project.service.ProjectService;
import com.example.sideproject.domain.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final ProjectService projectService;
    private final ResumeService resumeService;


}
