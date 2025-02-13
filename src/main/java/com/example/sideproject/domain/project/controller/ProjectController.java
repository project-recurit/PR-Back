package com.example.sideproject.domain.project.controller;

import com.example.sideproject.domain.project.dto.*;
import com.example.sideproject.domain.project.service.ProjectService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    // 팀 모집 생성
    @PostMapping
    public ResponseEntity<ResponseMessageDto> createProject(@ModelAttribute ProjectRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        projectService.createTeamRecruit(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_TEAM_RECRUIT_SUCCESS));
    }

    // 팀 모집 상세 조회
    @GetMapping("/{projectId}")
    public ResponseEntity<ResponseDataDto<ProjectDetailResponseDto>> getProject(@PathVariable("projectId") Long projectId) {
        ProjectDetailResponseDto responseDto = projectService.getProject(projectId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_TEAM_RECRUIT_SUCCESS, responseDto));
    }

    // 팀 모집 전체 조회 (페이지네이션)
    @GetMapping
    public ResponseEntity<ResponseDataDto<Page<ProjectsResponseDto>>> getProjects(
            @RequestParam(required = false, defaultValue = "1", name = "page") int page) {
        Page<ProjectsResponseDto> responseDto = projectService.getProjects(page);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_TEAM_RECRUIT_SUCCESS, responseDto));
    }

    // 팀 모집 수정
    @PutMapping("/{teamRecruitId}")
    public ResponseEntity<ResponseMessageDto> updateTeamRecruit(
            @PathVariable Long teamRecruitId,
            @RequestBody ProjectRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        projectService.updateTeamRecruit(teamRecruitId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.UPDATE_TEAM_RECRUIT_SUCCESS));
    }

    // 팀 모집 삭제
    @DeleteMapping("/{teamRecruitId}")
    public ResponseEntity<ResponseMessageDto> deleteTeamRecruit(
            @PathVariable Long teamRecruitId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        projectService.deleteTeamRecruit(teamRecruitId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_TEAM_RECRUIT_SUCCESS));
    }


}

