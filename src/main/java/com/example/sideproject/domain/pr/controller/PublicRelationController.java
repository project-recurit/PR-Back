package com.example.sideproject.domain.pr.controller;

import com.example.sideproject.domain.pr.dto.read.PublicRelationListResponseDto;
import com.example.sideproject.domain.pr.dto.search.SearchPublicRelationRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sideproject.domain.pr.dto.CreatePublicRelationRequestDto;
import com.example.sideproject.domain.pr.dto.PublicRelationDto;
import com.example.sideproject.domain.pr.service.PublicRelationService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.sideproject.global.security.UserDetailsImpl;

@RestController
@RequiredArgsConstructor    
@RequestMapping("/api/v1/pr")
public class PublicRelationController {
    private final PublicRelationService publicRelationService;

    @PostMapping
    public ResponseEntity<ResponseDataDto<PublicRelationDto>> createPublicRelation(
            @RequestBody CreatePublicRelationRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PublicRelationDto responseDto = publicRelationService.createPublicRelation(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.CREATE_PROJECT_RECRUIT_SUCCESS, responseDto));
    }

    @GetMapping("/{publicRelationId}")
    public ResponseEntity<ResponseDataDto<PublicRelationDto>> getPublicRelation(@PathVariable Long publicRelationId) {
        PublicRelationDto responseDto = publicRelationService.getPublicRelation(publicRelationId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_PROJECT_RECRUIT_SUCCESS, responseDto));
    }

    @GetMapping
    public ResponseEntity<ResponseDataDto<PagedModel<PublicRelationListResponseDto>>> getPublicRelations(SearchPublicRelationRequest request,
                                                                                                         Pageable pageable) {
        PagedModel<PublicRelationListResponseDto> responseDto = publicRelationService.getPublicRelations(request, pageable);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_PROJECT_RECRUIT_SUCCESS, responseDto));
    }

    @PutMapping("/{publicRelationId}")
    public ResponseEntity<ResponseMessageDto> updatePublicRelation(
            @PathVariable Long publicRelationId,
            @RequestBody CreatePublicRelationRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        publicRelationService.updatePublicRelation(publicRelationId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.UPDATE_PROJECT_RECRUIT_SUCCESS));
    }

    @DeleteMapping("/{publicRelationId}")
    public ResponseEntity<ResponseMessageDto> deletePublicRelation(
            @PathVariable Long publicRelationId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        publicRelationService.deletePublicRelation(publicRelationId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_PROJECT_RECRUIT_SUCCESS));
    }
}
