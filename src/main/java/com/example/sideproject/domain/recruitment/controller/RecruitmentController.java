package com.example.sideproject.domain.recruitment.controller;

import com.example.sideproject.domain.recruitment.dto.*;
import com.example.sideproject.domain.recruitment.entity.RecruitmentImage;
import com.example.sideproject.domain.recruitment.service.RecruitmentService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "프로젝트 구인 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruitment")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    // 팀 모집 생성
    @Operation(summary = "프로젝트 구인 글 생성", description = "contact 제외 모두 필수 값")
    @PostMapping()
    public ResponseEntity<ResponseMessageDto> createRecruitment(@RequestPart(name = "recruitment") RecruitmentRequestDto recruitment,
                                                                @RequestPart(name = "files",required = false) List<MultipartFile> files,
                                                                @RequestPart(name = "positions",required = false) List<RecruitmentPositionRequestDto> positions,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        recruitmentService.createRecruitment(recruitment, files, positions, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_TEAM_RECRUIT_SUCCESS));
    }

    // 팀 모집 상세 조회
    @Operation(summary = "프로젝트 구인 글 상세 조회", description = "존재하지 않는 id값 조회 시 404에러")
    @GetMapping("/{recruitmentId}")
    public ResponseEntity<ResponseDataDto<RecruitmentDetailResponseDto>> getRecruitment(@PathVariable("recruitmentId") Long recruitmentId) {
        RecruitmentDetailResponseDto responseDto = recruitmentService.getRecruitment(recruitmentId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_TEAM_RECRUIT_SUCCESS, responseDto));
    }

    // 팀 모집 전체 조회 (페이지네이션)
    @Operation(summary = "프로젝트 구인 글 전체 조회", description = "페이지 기본 값 = 1, 없는 페이지 조회하면 content 부분 빈 배열나옴.")
    @GetMapping
    public ResponseEntity<ResponseDataDto<Page<RecruitmentsResponseDto>>> getRecruitments(
            @RequestParam(required = false, defaultValue = "1", name = "page") int page) {
        Page<RecruitmentsResponseDto> responseDto = recruitmentService.getRecruitments(page);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_TEAM_RECRUIT_SUCCESS, responseDto));
    }

    // 팀 모집 수정
    @Operation(summary = "프로젝트 구인 글 수정", description = "contact와 existFiles를 제외한 모든 필드 넣어야함, 다른 유저가 시도할 시 에러")
    @PutMapping("/{recruitmentId}")
    public ResponseEntity<ResponseMessageDto> updateRecruitment(@PathVariable("recruitmentId") Long recruitmentId,
                                                                @ModelAttribute RecruitmentUpdateDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        recruitmentService.updateRecruitment(recruitmentId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.UPDATE_TEAM_RECRUIT_SUCCESS));
    }

    // 팀 모집 삭제
    @Operation(summary = "프로젝트 구인 글 삭제", description = "다른 유저가 시도할 시 에러, 없는 id값 넣으면 에러")
    @DeleteMapping("/{recruitmentId}")
    public ResponseEntity<ResponseMessageDto> deleteRecruitment(@PathVariable("recruitmentId") Long recruitmentId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        recruitmentService.deleteRecruitment(recruitmentId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_TEAM_RECRUIT_SUCCESS));
    }
}

