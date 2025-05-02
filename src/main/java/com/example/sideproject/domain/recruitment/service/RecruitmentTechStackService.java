package com.example.sideproject.domain.recruitment.service;

import com.example.sideproject.domain.recruitment.entity.RecruitmentTechStack;
import com.example.sideproject.domain.recruitment.repository.RecruitmentTechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentTechStackService {

    private final RecruitmentTechStackRepository recruitmentTechStackRepository;
    public void createRecruitmentTechStack(List<RecruitmentTechStack> recruitmentTechStacks) {
        recruitmentTechStackRepository.saveAll(recruitmentTechStacks);
    }

    public List<RecruitmentTechStack> getRecruitmentTechStacks (Long recruitmentId) {
        return recruitmentTechStackRepository.findAllByRecruitmentId(recruitmentId);
    }

    public void deleteRecruitmentTechStacks(Long recruitmentId) {
        recruitmentTechStackRepository.deleteAllByRecruitmentId(recruitmentId);
    }
}
