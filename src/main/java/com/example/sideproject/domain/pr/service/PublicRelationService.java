package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.pr.dto.CreatePublicRelationRequestDto;
import com.example.sideproject.domain.pr.dto.CreatePublicRelationDetailRequestDto;
import com.example.sideproject.domain.pr.dto.PublicRelationDto;
import com.example.sideproject.domain.pr.dto.read.PublicRelationListResponseDto;
import com.example.sideproject.domain.pr.dto.search.SearchPublicRelationRequest;
import com.example.sideproject.domain.pr.entity.PublicRelation;
import com.example.sideproject.domain.pr.entity.PublicRelationDetail;
import com.example.sideproject.domain.pr.repository.PublicRelationRepository;
import com.example.sideproject.domain.pr.repository.query.PublicRelationQueryRepository;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.global.exception.CustomException;
import com.example.sideproject.global.enums.ErrorType;  
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional
public class PublicRelationService {
    private final UserRepository userRepository;
    private final PublicRelationRepository publicRelationRepository;
    private final PublicRelationQueryRepository publicRelationQueryRepository;

    @Transactional
    public PublicRelationDto createPublicRelation(CreatePublicRelationRequestDto requestDto, User user) {
        User foundUser = validateActiveUser(user);
        PublicRelation pr = requestDto.toEntity(foundUser);
        pr.addDetails();
        return PublicRelationDto.of(publicRelationRepository.save(pr));
    }

    @Transactional
    public PublicRelationDto getPublicRelation(Long projectRecruitId) {
        PublicRelation publicRelation = findPublicRelation(projectRecruitId);
        publicRelation.plusViewCount();
        return PublicRelationDto.of(publicRelation);
    }

    @Transactional(readOnly = true)
    public PagedModel<PublicRelationListResponseDto> getPublicRelations(SearchPublicRelationRequest request, Pageable pageable) {
        Page<PublicRelationListResponseDto> publicRelations = publicRelationQueryRepository.findPublicRelationList(request, pageable);
        return new PagedModel<>(publicRelations);
    }

    @Transactional
    public void updatePublicRelation(Long projectRecruitId, CreatePublicRelationRequestDto requestDto, User user) {
        User foundUser = validateActiveUser(user);
        PublicRelation publicRelation = findPublicRelation(projectRecruitId);
        validatePublicRelationOwner(publicRelation, foundUser);

        List<PublicRelationDetail> details = requestDto.prDetails()
                .stream().map(CreatePublicRelationDetailRequestDto::toEntity).toList();

        publicRelation.update(requestDto.title(),
                details,
                requestDto.techStacks()
        );
    }

    public void deletePublicRelation(Long projectRecruitId, User user) {
        User foundUser = validateActiveUser(user);
        PublicRelation publicRelation = findPublicRelation(projectRecruitId);
        validatePublicRelationOwner(publicRelation, foundUser);

        publicRelationRepository.delete(publicRelation);
    }

    private User validateActiveUser(User user) {
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

        if (foundUser.getUserStatus() != UserStatus.ACTIVE_USER) {
            throw new CustomException(ErrorType.WITHDRAW_USER);
        }

        return foundUser;
    }

    private PublicRelation findPublicRelation(Long projectRecruitId) {
        return publicRelationRepository.findById(projectRecruitId)
                .orElseThrow(() -> new CustomException(ErrorType.PROJECT_RECRUIT_NOT_FOUND));
    }

    private void validatePublicRelationOwner(PublicRelation publicRelation, User user) {
        if (!Objects.equals(publicRelation.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorType.NOT_YOUR_POST);
        }
    }
}