package com.example.sideproject.domain.recruitment.service;

import com.example.sideproject.domain.notification.service.RecruitmentNotificationService;
import com.example.sideproject.domain.recruitment.dto.RecruitmentDetailResponseDto;
import com.example.sideproject.domain.recruitment.dto.RecruitmentRequestDto;
import com.example.sideproject.domain.recruitment.dto.RecruitmentUpdateDto;
import com.example.sideproject.domain.recruitment.dto.RecruitmentsResponseDto;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentImage;
import com.example.sideproject.domain.recruitment.entity.RecruitmentTechStack;
import com.example.sideproject.domain.recruitment.repository.RecruitmentRepository;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final UserRepository userRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentNotificationService recruitmentNotificationService; //알림
    private final RecruitmentTechStackService recruitmentTechStackService; // 모집공고 기술스택
    private final RecruitmentImageService recruitmentImageService; // 모집공고 이미지
    private final TechStackRepository techStackRepository; // 임시
    private final RecruitmentQueryRepository recruitmentQueryRepository; // 동적쿼리
    private final RecruitmentPositionService recruitmentPositionService; // 모집공고 포지션
//    private final SearchService searchService;
//    private final SearchProjectRepository searchProjectRepository;

    /**
     * 프로젝트 구인 글 생성
     */
    @Transactional
    public void createRecruitment(RecruitmentRequestDto requestDto, User user) {

        final User foundUser = validateActiveUser(user);
        final Recruitment recruitment = requestDto.toEntity(foundUser);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);
//        searchService.saveRecruitment(savedRecruitment);

        List<TechStack> techStacks = new ArrayList<>();
        List<Long> techStackIds = new ArrayList<>();

        if (!requestDto.recruitmentTechStacks().isEmpty()) {

            // 테크스텍 있는거만 검증 한 후 List 반환
            // findAllById는 쿼리를 직접짠거랑 많이 다른게 없어서 적용
            techStacks = techStackRepository.findAllById(requestDto.recruitmentTechStacks());
            List<RecruitmentTechStack> recruitmentTechStacks = new ArrayList<>();

            for (TechStack techStack : techStacks) {
                // 배열에 미리 넣어두기
                recruitmentTechStacks.add(
                        RecruitmentTechStack.builder()
                                .techStack(techStack)
                                .recruitment(recruitment)
                                .build()
                );
                techStackIds.add(techStack.getId());
            }
            // 이 메서드 안에 saveAll
            recruitmentTechStackService.createRecruitmentTechStack(recruitmentTechStacks);
        }

        // 구인공고 이미지
        if (requestDto.files() != null) {
            for (MultipartFile url : requestDto.files()) {
                recruitmentImageService.createRecruitmentImage(recruitment, url);
            }
        }

        // 구인공고 직무
        if (!requestDto.positions().isEmpty()) {
            recruitmentPositionService.createPosition(recruitment,requestDto.positions());
        }

        // 기술스택에 해당하는 유저를 조회 (알림)
        List<User> users = findUserByTechStacks(techStacks);
        recruitmentNotificationService.notice(recruitment, users, techStackIds);
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
    public void updateRecruitment(Long recruitmentId, RecruitmentUpdateDto requestDto, User user) {

        User foundUser = validateActiveUser(user);
        Recruitment recruitment = findRecruitment(recruitmentId);
        validateTeamRecruitOwner(recruitment, foundUser);

        // ----------------------------------- image Url -------------------------------------------
        // 기존 이미지 URL
        List<RecruitmentImage> existImageUrls = recruitmentImageService.existImageUrls(recruitmentId);

        // 기존 이미지중 삭제된거 있는 지 확인 후 삭제하기
        if (!existImageUrls.isEmpty() && existImageUrls.size() != requestDto.existFiles().size()) {
            existImageUrls.removeIf(recruitmentImage -> !requestDto.existFiles().contains(recruitmentImage.getId()));
        }

        // 새로운 파일이 존재하면 추가
        if (requestDto.newFiles() != null && !requestDto.newFiles().isEmpty()) {
            for (MultipartFile file : requestDto.newFiles()) {
                RecruitmentImage recruitmentImage = recruitmentImageService.createRecruitmentImage(recruitment, file);
                existImageUrls.add(recruitmentImage);
            }
        }

        // ---------------------------------------- techStack --------------------------------------
        List<RecruitmentTechStack> recruitmentTechStacks = new ArrayList<>();
        List<TechStack> techStacks = techStackRepository.findAllById(requestDto.recruitmentTechStacks());

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

        Recruitment updateRecruitment = requestDto.update(user, recruitmentId, recruitmentTechStacks, existImageUrls);
        Recruitment savedRecruitment = recruitmentRepository.save(updateRecruitment);
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


