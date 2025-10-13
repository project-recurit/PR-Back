package com.example.sideproject.domain.status.project.controller;

import com.example.sideproject.domain.applicant.service.ApplicantService;
import com.example.sideproject.domain.status.project.dto.StatusApplicantResponseDto;
import com.example.sideproject.domain.status.project.dto.StatusSearchRequest;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "프로젝트 지원 현황 api", description = "프로젝트 지원 현황 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/status/applicant")
public class ApplicantStatusController {
    private final ApplicantService applicantService;

    @Operation(summary = "내 프로젝트 지원 현황 조회", description = "로그인한 사용자의 프로젝트 지원 현황을 조회한다.")
    @GetMapping
    public ResponseEntity<ResponseDataDto<PagedModel<StatusApplicantResponseDto>>> getMyApplications(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            StatusSearchRequest searchRequest
    ) {
        PagedModel<StatusApplicantResponseDto> res = applicantService.getMyApplications(userDetails.getUser(), searchRequest);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, res));
    }
}
