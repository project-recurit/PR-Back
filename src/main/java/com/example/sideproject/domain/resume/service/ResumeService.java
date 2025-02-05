package com.example.sideproject.domain.resume.service;

import com.example.sideproject.domain.resume.dto.ResumeRequestDto;
import com.example.sideproject.domain.resume.dto.ResumeResponseDto;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.resume.repository.ResumeRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;

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
        resume.setPublished(true);
    }

    @Transactional
    public void unPublish(User user, Long resumeId) {
        Resume resume = getResumeByIdAndUser(resumeId, user);
        resume.setPublished(false);
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
}
