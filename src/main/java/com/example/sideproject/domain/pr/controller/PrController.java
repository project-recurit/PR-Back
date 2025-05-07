package com.example.sideproject.domain.pr.controller;

import com.example.sideproject.domain.pr.dto.*;
import com.example.sideproject.domain.pr.service.PrService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "pr api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/pr")
public class PrController {
    private final PrService prService;

    @Operation(summary = "pr 게시글 작성", description = "pr 게시글 작성")
    @PostMapping
    public ResponseEntity<ResponseDataDto<Long>> savePr(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody PrRequest prRequest) {
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, prService.savePr(userDetails.getUser(), prRequest)));
    }

    @Operation(summary = "pr 게시글 리스트 조회", description = "해당하는 pr 게시글 조회")
    @GetMapping
    public ResponseEntity<ResponseDataDto<PagedModel<PrListResponseDto>>> getPrs(Pageable pageable,
                                                                                 PrSort prSort,
                                                                                 PrSearchRequest prSearchRequest) {
        PagedModel<PrListResponseDto> prs = prService.getPrs(pageable, prSort, prSearchRequest);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, prs));
    }

    @Operation(summary = "pr 게시글 조회", description = "해당하는 pr 게시글 상세 조회")
    @GetMapping("/{prId}")
    public ResponseEntity<ResponseDataDto<PrResponse>> getPr(@PathVariable("prId") Long prId) {
        PrResponse pr = prService.read(prId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, pr));
    }

    @Operation(summary = "pr 게시글 수정", description = "해당하는 pr 게시글 수정")
    @PutMapping("/{prId}")
    public ResponseEntity<ResponseDataDto<Long>> updatePr(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @PathVariable("prId") Long prId,
                                                          @RequestBody PrRequest prRequest) {
        Long res = prService.updatePr(userDetails.getUser(), prId, prRequest);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, res));
    }

    @Operation(summary = "pr 게시글 삭제", description = "해당하는 pr 게시글 삭제")
    @DeleteMapping("/{prId}")
    public ResponseEntity<ResponseMessageDto> deletePr(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable("prId") Long prId) {
        prService.deletePr(userDetails.getUser(), prId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS));
    }
}
