package com.example.sideproject.domain.recruitment.service;

import com.example.sideproject.domain.notification.service.RecruitmentNotificationService;
import com.example.sideproject.domain.recruitment.dto.*;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentImage;
import com.example.sideproject.domain.recruitment.entity.RecruitmentPosition;
import com.example.sideproject.domain.recruitment.entity.RecruitmentTechStack;
import com.example.sideproject.domain.recruitment.repository.RecruitmentPositionRepository;
import com.example.sideproject.domain.recruitment.repository.RecruitmentRepository;
import com.example.sideproject.domain.recruitment.repository.RecruitmentTechStackRepository;
import com.example.sideproject.domain.recruitment.repository.query.RecruitmentQueryRepository;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.repository.TechStackRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class
RecruitmentService {
    private final UserRepository userRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentNotificationService recruitmentNotificationService; //알림
    private final RecruitmentImageService recruitmentImageService; // 모집공고 이미지
    private final TechStackRepository techStackRepository; // 임시
    private final RecruitmentQueryRepository recruitmentQueryRepository; // 동적쿼리
    private final RecruitmentPositionRepository recruitmentPositionRepository;
    private final RecruitmentTechStackRepository recruitmentTechStackRepository;
//    private final SearchService searchService;
//    private final SearchProjectRepository searchProjectRepository;

    /**
     * 프로젝트 구인 글 생성
     */
    @Transactional
    public void createRecruitment(RecruitmentRequestDto requestDto, List<MultipartFile> files,
                                  List<RecruitmentPositionRequestDto> positions, User user) {

        final User foundUser = validateActiveUser(user);
        final Recruitment recruitment = Recruitment.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .deadLine(requestDto.getDeadLine())
                .estimatedDuration(requestDto.getEstimatedDuration())
                .workType(requestDto.getWorkType())
                .recruitmentCategory(requestDto.getRecruitmentCategory())
                .isCommercial(requestDto.isCommercial())
                .isRecruiting(true)
                .user(foundUser)
                .viewCount(0)
                .commentCount(0)
                .favoriteCount(0)
                .build();
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

        // 1. 직무 추가
        for (RecruitmentPositionRequestDto positionDto : positions) {
            RecruitmentPosition position = RecruitmentPosition.builder()
                    .recruitment(recruitment)
                    .capacity(positionDto.getCapacity())
                    .position(positionDto.getPosition())
                    .build();
            recruitment.addPosition(position); // 연관관계 설정
        }

        // 2. 기술 스택 처리
        List<TechStack> techStacks = techStackRepository.findAllById(requestDto.getTechStackIds());
        for (TechStack techStack : techStacks) {
            recruitment.addRecruitmentTechStacks(
                    RecruitmentTechStack.builder()
                            .techStack(techStack)
                            .recruitment(recruitment)
                            .build()
            );
        }

        // 3. 구인공고 이미지
        if (files != null) {
            for (MultipartFile url : files) {
                recruitmentImageService.createRecruitmentImage(recruitment, url);
            }
        }

        // 4. 알림
        List<User> users = findUserByTechStacks(techStacks);
        recruitmentNotificationService.notice(savedRecruitment, users, techStacks.stream().map(TechStack::getId).toList());
    }

    public List<User> findUserByTechStacks(List<TechStack> techStacks) {
        return userRepository.findByUserTechStacks_TechStackIn(techStacks);
    }

    /**
     * 게시글 상세 조회
     * 조회 시 viewCount + 1
     */
    @Transactional
    public RecruitmentDetailResponseDto getRecruitment(Long recruitmentId) {
        return recruitmentQueryRepository.getRecruitment(recruitmentId);
    }

    /**
     * 게시글 전체 조회
     */
    public Page<RecruitmentsResponseDto> getRecruitments(int page) {

        final Pageable pageable = PageRequest.of(page - 1, 20);

        return recruitmentQueryRepository.getRecruitments(pageable);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void updateRecruitment(Long recruitmentId, RecruitmentUpdateDto requestDto, List<MultipartFile> newFiles, List<RecruitmentPositionRequestDto> positions, User user) {

        User foundUser = validateActiveUser(user);
        Recruitment recruitment = findRecruitment(recruitmentId);
        validateTeamRecruitOwner(recruitment, foundUser);

        // ----------------------------------- image Url -------------------------------------------
        // 기존 이미지 URL
        List<RecruitmentImage> existImageUrls = recruitmentImageService.existImageUrls(recruitmentId);

        // 기존 이미지중 삭제된거 있는 지 확인 후 삭제하기
        if (!existImageUrls.isEmpty() && existImageUrls.size() != requestDto.existFiles().size()) {
            existImageUrls.removeIf(
                    recruitmentImage -> !requestDto.existFiles().contains(recruitmentImage.getId()));
        }

        // 새로운 파일이 존재하면 추가
        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                RecruitmentImage recruitmentImage = recruitmentImageService.createRecruitmentImage(recruitment, file);
                existImageUrls.add(recruitmentImage);
            }
        }

        // 기존 포지션, 기술스택 리스트 삭제
        recruitment.clearList();
        // ---------------------------------------- techStack --------------------------------------
        List<RecruitmentTechStack> recruitmentTechStacks = new ArrayList<>();
        List<TechStack> techStacks = techStackRepository.findAllById(requestDto.techStackIds());

        // 모듈화 필요해보임
        for (TechStack techStack : techStacks) {
            // 배열에 미리 넣어두기
            recruitmentTechStacks.add(
                    RecruitmentTechStack.builder()
                            .techStack(techStack)
                            .recruitment(recruitment)
                            .build()
            );
        }

        // ---------------------------------------- position --------------------------------------

        List<RecruitmentPosition> recruitmentPositions = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (RecruitmentPositionRequestDto position : positions) {
                recruitmentPositions.add(
                        RecruitmentPosition.builder()
                                .position(position.getPosition())
                                .capacity(position.getCapacity())
                                .recruitment(recruitment)
                                .build()
                );
            }
        } // 다시 인서트

        Recruitment updateRecruitment = requestDto.update(user, recruitmentId, recruitmentTechStacks, existImageUrls, recruitmentPositions);
        recruitmentRepository.save(updateRecruitment);
//        searchService.saveRecruitment(savedRecruitment);
    }

    /**
     * 게시글 삭제
     */
    public void deleteRecruitment(Long recruitmentId, User user) {
        User foundUser = validateActiveUser(user);
        Recruitment recruitment = findRecruitment(recruitmentId);
        validateTeamRecruitOwner(recruitment, foundUser);

        recruitmentRepository.delete(recruitment);
        // 이미 recruitment를 지웠기 때문에 받은 recruitmentId로 해당하는 엘라스틱서치의 document삭제
//        searchrecruitmentRepository.deleteById(recruitmentId);
    }


    private User validateActiveUser(User user) {
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

        if (foundUser.getUserStatus() != UserStatus.ACTIVE_USER) {
            throw new CustomException(ErrorType.WITHDRAW_USER);
        }

        return foundUser;
    }

    public Recruitment findRecruitment(Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new CustomException(ErrorType.TEAM_RECRUIT_NOT_FOUND));
    }

    private void validateTeamRecruitOwner(Recruitment recruitment, User user) {
        if (!Objects.equals(recruitment.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorType.NOT_YOUR_POST);
        }
    }
}


