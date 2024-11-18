package com.example.sideproject.domain.team.controller;

import com.example.sideproject.domain.team.dto.CreateTeamRecruitRequestDto;
import com.example.sideproject.domain.team.dto.CreateTeamRecruitResponseDto;
import com.example.sideproject.domain.team.service.TeamRecruitService;
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

@RestController
@RequiredArgsConstructor    
@RequestMapping("/api/v1/team-recruit")
public class TeamRecruitController {

    private final TeamRecruitService teamRecruitService;

    // 팀 모집 생성
    @PostMapping
    public ResponseEntity<ResponseMessageDto> createTeamRecruit(@RequestBody CreateTeamRecruitRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        teamRecruitService.createTeamRecruit(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_TEAM_RECRUIT_SUCCESS));
    }

    // 팀 모집 상세 조회
    @GetMapping("/{teamRecruitId}")
    public ResponseEntity<ResponseDataDto<CreateTeamRecruitResponseDto>> getTeamRecruit(@PathVariable Long teamRecruitId){
        CreateTeamRecruitResponseDto responseDto = teamRecruitService.getTeamRecruit(teamRecruitId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_TEAM_RECRUIT_SUCCESS, responseDto));
    }

    // 팀 모집 수정
    @PutMapping("/{teamRecruitId}")
    public ResponseEntity<ResponseMessageDto> updateTeamRecruit(@PathVariable Long teamRecruitId, @RequestBody CreateTeamRecruitRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        teamRecruitService.updateTeamRecruit(teamRecruitId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.UPDATE_TEAM_RECRUIT_SUCCESS));
    }

    // 팀 모집 삭제
    @DeleteMapping("/{teamRecruitId}")
    public ResponseEntity<ResponseMessageDto> deleteTeamRecruit(@PathVariable Long teamRecruitId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        teamRecruitService.deleteTeamRecruit(teamRecruitId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_TEAM_RECRUIT_SUCCESS));
    }

    


}

