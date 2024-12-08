package com.example.sideproject.domain.personal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sideproject.domain.personal.dto.CreateProjectRecruitRequestDto;
import com.example.sideproject.domain.personal.dto.CreateProjectRecruitResponseDto;
import com.example.sideproject.domain.personal.service.ProjectRecruitService;  
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.sideproject.global.security.UserDetailsImpl;

@RestController
@RequiredArgsConstructor    
@RequestMapping("/api/v1/project-recruit")
public class ProjectRecruitController {
    private final ProjectRecruitService projectRecruitService;

    @PostMapping
    public ResponseEntity<ResponseMessageDto> createProjectRecruit(
            @RequestBody CreateProjectRecruitRequestDto requestDto, 
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        projectRecruitService.createProjectRecruit(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_PROJECT_RECRUIT_SUCCESS));
    }

    @GetMapping("/{projectRecruitId}")
    public ResponseEntity<ResponseDataDto<CreateProjectRecruitResponseDto>> getProjectRecruit(
            @PathVariable Long projectRecruitId) {
        CreateProjectRecruitResponseDto responseDto = projectRecruitService.getProjectRecruit(projectRecruitId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_PROJECT_RECRUIT_SUCCESS, responseDto));
    }

    @PutMapping("/{projectRecruitId}")
    public ResponseEntity<ResponseMessageDto> updateProjectRecruit(
            @PathVariable Long projectRecruitId, 
            @RequestBody CreateProjectRecruitRequestDto requestDto, 
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        projectRecruitService.updateProjectRecruit(projectRecruitId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.UPDATE_PROJECT_RECRUIT_SUCCESS));
    }

    @DeleteMapping("/{projectRecruitId}")
    public ResponseEntity<ResponseMessageDto> deleteProjectRecruit(
            @PathVariable Long projectRecruitId, 
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        projectRecruitService.deleteProjectRecruit(projectRecruitId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_PROJECT_RECRUIT_SUCCESS));
    }
}
