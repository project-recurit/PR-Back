package com.example.sideproject.domain.applicant.controller;

import com.example.sideproject.domain.applicant.dto.ApplicantRequestDto;
import com.example.sideproject.domain.applicant.dto.ApplicantResponseDto;
import com.example.sideproject.domain.applicant.dto.search.SearchApplicantDto;
import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.applicant.service.ApplicantService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/{projectId}/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;

    @GetMapping
    public ResponseEntity<ResponseDataDto<List<ApplicantResponseDto>>> getApplicants(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                    @PathVariable("projectId") Long projectId,
                                                                                    SearchApplicantDto searchDto) {
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, applicantService.getApplicants(userDetails.getUser(), projectId, searchDto)));
    }

    @PostMapping
    public ResponseEntity<ResponseDataDto<Long>> apply(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable("projectId") Long projectId){
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, applicantService.apply(userDetails.getUser(), projectId)));
    }

    @PutMapping("/{applicantId}")
    public ResponseEntity<ResponseDataDto<Void>> updateStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable("projectId") Long projectId,
                                                              @PathVariable("applicantId") Long applicantId,
                                                              @RequestBody ApplicantRequestDto req){
        applicantService.updateStatus(userDetails.getUser(), projectId, applicantId, req.status());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, null));
    }

    @DeleteMapping("/{applicantId}")
    public ResponseEntity<ResponseDataDto<Void>> cancel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable("projectId") Long projectId,
                                                        @PathVariable("applicantId") Long applicantId){
        applicantService.cancel(userDetails.getUser(), projectId, applicantId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, null));
    }
}
