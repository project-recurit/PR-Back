package com.example.sideproject.domain.recruitment.service;

import com.example.sideproject.domain.favorite.FavoriteService;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentFavorite;
import com.example.sideproject.domain.recruitment.repository.RecruitmentFavoriteRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class RecruitmentFavoriteService extends FavoriteService<RecruitmentFavorite> {
    private final RecruitmentService recruitmentService;
    private final RecruitmentFavoriteRepository recruitmentRepository;

    public RecruitmentFavoriteService(ApplicationEventPublisher publisher, RecruitmentService recruitmentService,
                                      RecruitmentFavoriteRepository recruitmentRepository) {
        super(publisher);
        this.recruitmentService = recruitmentService;
        this.recruitmentRepository = recruitmentRepository;
    }

    @Override
    protected RecruitmentFavorite save(User user, Long targetId) {
        if (recruitmentRepository.existsByUserAndRecruitment_Id(user, targetId)) {
            throw new CustomException(ErrorType.ALREADY_EXIST_FAVORITE);
        }
        Recruitment recruitment = recruitmentService.findRecruitment(targetId);
        RecruitmentFavorite favorite = RecruitmentFavorite.builder()
                .user(user)
                .recruitment(recruitment)
                .build();
        return recruitmentRepository.save(favorite);
    }

    @Override
    protected Long delete(User user, Long id) {
        RecruitmentFavorite favorite = getFavorite(id);
        if (!favorite.isOwn(user.getId())) {
            throw new CustomException(ErrorType.NOT_OWNER);
        }
        Long targetId = favorite.getTargetId();
        recruitmentRepository.delete(favorite);
        return targetId;
    }

    @Override
    public RecruitmentFavorite getFavorite(Long id) {
        return recruitmentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorType.RECRUITMENT_FAVORITE_NOT_FOUND)
        );
    }
}
