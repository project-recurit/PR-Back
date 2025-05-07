package com.example.sideproject.domain.recruitment.service;

import com.example.sideproject.domain.recruitment.dto.RecruitmentPositionRequestDto;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentPosition;
import com.example.sideproject.domain.recruitment.repository.RecruitmentPositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecruitmentPositionService {

    private final RecruitmentPositionRepository positionRepository;

    public void createPosition(Recruitment recruitment, List<RecruitmentPositionRequestDto> recruitmentPosition) {
        for (RecruitmentPositionRequestDto a : recruitmentPosition) {
            positionRepository.save(RecruitmentPosition.builder()
                    .position(a.getPosition())
                    .capacity(a.getCapacity())
                    .recruitment(recruitment)
                    .build());
        }
    }
}
