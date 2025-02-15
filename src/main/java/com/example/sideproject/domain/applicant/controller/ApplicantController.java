package com.example.sideproject.domain.applicant.controller;

import com.example.sideproject.domain.applicant.dto.ApplicantApplyDto;
import com.example.sideproject.domain.applicant.dto.ApplicantRequestDto;
import com.example.sideproject.domain.applicant.dto.ApplicantResponseDto;
import com.example.sideproject.domain.applicant.dto.search.SearchApplicantDto;
import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.applicant.service.ApplicantService;
import com.example.sideproject.domain.resume.dto.ResumeResponseDto;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "프로젝트 지원 api", description = "프로젝트 지원 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/{projectId}/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;

    @Operation(summary = "지원서 조회", description = "해당하는 프로젝트의 지원서를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = ApplicantResponseDto.class))}),
    })
    @GetMapping
    public ResponseEntity<ResponseDataDto<List<ApplicantResponseDto>>> getApplicants(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                    @PathVariable("projectId") Long projectId,
                                                                                    SearchApplicantDto searchDto) {
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, applicantService.getApplicants(userDetails.getUser(), projectId, searchDto)));
    }

    @Operation(summary = "프로젝트 지원", description = "프로젝트에 지원한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "400", description = "이미 지원한 사용자입니다."),
            @ApiResponse(responseCode = "404", description = "팀 모집글을 찾을 수 없습니다."),
    })
    @PostMapping
    public ResponseEntity<ResponseDataDto<Long>> apply(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable("projectId") Long projectId,
                                                       @RequestBody ApplicantApplyDto req){
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, applicantService.apply(userDetails.getUser(), projectId, req)));
    }

    @Operation(summary = "프로젝트 지원 상태 변경", description = "지원한 사람의 지원 상태를 변경한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "팀 모집글을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "404-1", description = "존재하지 않는 지원서입니다."),
    })
    @PutMapping("/{applicantId}")
    public ResponseEntity<ResponseDataDto<Void>> updateStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable("projectId") Long projectId,
                                                              @PathVariable("applicantId") Long applicantId,
                                                              @RequestBody ApplicantRequestDto req){
        applicantService.updateStatus(userDetails.getUser(), projectId, applicantId, req.status());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, null));
    }

    @Operation(summary = "프로젝트 지원 취소", description = "지원한 프로젝트의 지원을 취소한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "팀 모집글을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "404-1", description = "존재하지 않는 지원서입니다."),
    })
    @DeleteMapping("/{applicantId}")
    public ResponseEntity<ResponseDataDto<Void>> cancel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable("projectId") Long projectId,
                                                        @PathVariable("applicantId") Long applicantId){
        applicantService.cancel(userDetails.getUser(), projectId, applicantId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, null));
    }
}
