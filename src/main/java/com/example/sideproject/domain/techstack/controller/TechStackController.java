package com.example.sideproject.domain.techstack.controller;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.service.TechStackService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.enums.ResponseStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "기술스택 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tech-stack")
public class TechStackController {
    private final TechStackService techStackService;


    @GetMapping
    public ResponseEntity<ResponseDataDto<List<TechStackDto>>> getTechStackList(){
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, techStackService.getTeckStackList()));
    }

}
