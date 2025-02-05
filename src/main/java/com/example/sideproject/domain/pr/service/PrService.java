package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.pr.dto.PrResponseDto;
import com.example.sideproject.domain.resume.dto.ResumeResponseDto;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.resume.service.ResumeService;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrService {
    private final ResumeService resumeService;

    public PagedModel<PrResponseDto> getPrs(Pageable pageable) {
        Sort sort = Sort.by(Sort.Order.desc("publishedAt"));
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<PrResponseDto> publishedResumes = resumeService.getPublishedResumes(pageRequest);
        return new PagedModel<>(publishedResumes);
    }

    public ResumeResponseDto getPr(Long prId) {
        Resume resume = resumeService.getResumeById(prId);
        if (!resume.checkPublishStatus()) {
            throw new CustomException(ErrorType.UNPUBLISHED_RESUME);
        }

        return ResumeResponseDto.of(resume);
    }
}
