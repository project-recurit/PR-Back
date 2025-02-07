package com.example.sideproject.domain.project.controller;

import com.example.sideproject.domain.project.dto.CreateTeamRecruitRequestDto;
import com.example.sideproject.domain.project.dto.CreateTeamRecruitResponseDto;
import com.example.sideproject.domain.project.service.ProjectService;
import com.example.sideproject.global.security.UserDetailsImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.sideproject.domain.project.dto.CreateTeamRecruitPageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor    
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    // 팀 모집 생성
    @PostMapping
    public ResponseEntity<ResponseMessageDto> createTeamRecruit(@ModelAttribute CreateTeamRecruitRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        projectService.createTeamRecruit(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_TEAM_RECRUIT_SUCCESS));
    }

    // 팀 모집 상세 조회
    @GetMapping("/{teamRecruitId}")
    public ResponseEntity<ResponseDataDto<CreateTeamRecruitResponseDto>> getTeamRecruit(@PathVariable Long teamRecruitId) {
        CreateTeamRecruitResponseDto responseDto = projectService.getTeamRecruit(teamRecruitId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_TEAM_RECRUIT_SUCCESS, responseDto));
    }

    // 팀 모집 수정
    @PutMapping("/{teamRecruitId}")
    public ResponseEntity<ResponseMessageDto> updateTeamRecruit(
            @PathVariable Long teamRecruitId, 
            @RequestBody CreateTeamRecruitRequestDto requestDto, 
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

    // 팀 모집 전체 조회 (페이지네이션)
    @GetMapping
    public ResponseEntity<ResponseDataDto<CreateTeamRecruitPageResponseDto>> getTeamRecruits(
            @RequestParam(required = false, defaultValue = "0") String page) {
        CreateTeamRecruitPageResponseDto responseDto = projectService.getTeamRecruits(page);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_TEAM_RECRUIT_SUCCESS, responseDto));
    }
}

