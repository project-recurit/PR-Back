package com.example.sideproject.domain.resume.service;

import com.example.sideproject.domain.pr.dto.PrResponseDto;
import com.example.sideproject.domain.resume.dto.ResumeRequestDto;
import com.example.sideproject.domain.resume.dto.ResumeResponseDto;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.resume.repository.ResumeRepository;
import com.example.sideproject.domain.resume.repository.query.ResumeQueryRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final ResumeQueryRepository resumeQueryRepository;

    public ResumeResponseDto getResume(User user) {
        return ResumeResponseDto.of(getResumeByUser(user));
    }

    public Long saveResume(User user, ResumeRequestDto req) {
        if (resumeRepository.existsByUser(user)) {
            throw new CustomException(ErrorType.DUPLICATE_RESUME);
        }
        Resume resume = req.toEntity(user);
        return resumeRepository.save(resume).getId();
    }

    @Transactional
    public void updateResume(User user, Long resumeId, ResumeRequestDto req) {
        Resume resume = getResumeByIdAndUser(resumeId, user);

        Resume newResume = req.toEntity(user);
        resume.update(newResume);
    }

    @Transactional
    public void publish(User user, Long resumeId) {
        Resume resume = getResumeByIdAndUser(resumeId, user);
        if (!resume.canPublish()) {
            throw new CustomException(ErrorType.PUBLISH_FAILED);
        }
        resume.setPublished(true);
    }

    @Transactional
    public void unPublish(User user, Long resumeId) {
        Resume resume = getResumeByIdAndUser(resumeId, user);
        resume.setPublished(false);
    }

    public Page<PrResponseDto> getPublishedResumes(Pageable pageable) {
        return resumeQueryRepository.getPublishedResumes(pageable);
    }

    public Resume getResumeByUser(User user) {
        return resumeRepository.findResumeByUser(user).orElseThrow(
                () -> new CustomException(ErrorType.RESUME_NOT_FOUND)
        );
    }

    public Resume getResumeByIdAndUser(Long resumeId, User user) {
        return resumeRepository.findResumeByIdAndUser(resumeId, user).orElseThrow(
                () -> new CustomException(ErrorType.RESUME_NOT_FOUND)
        );
    }

    public Resume getResumeById(Long resumeId) {
        return resumeRepository.findResumeById(resumeId).orElseThrow(
                () -> new CustomException(ErrorType.RESUME_NOT_FOUND)
        );
    }

    public Resume validateResume(Long resumeId, User user) {
        // NOTE 0215: 이력서 존재 여부 확인
        Resume resume = getResumeById(resumeId);

        // NOTE 0215: 이력서 공개 여부 확인
        if (resume.getPublishedAt() == null)
            throw new CustomException(ErrorType.NOT_PUBLISH_RESUME);

        // NOTE 0215: 자신의 이력서는 관심목록 추가 제외
        if (resume.getUser().equals(user))
            throw new CustomException(ErrorType.NOT_MODIFY_OWN);

        return resume;
    }
}
