package com.example.sideproject.domain.recruitment.service;

import com.example.sideproject.domain.favorite.Favorite;
import com.example.sideproject.domain.favorite.FavoriteDomain;
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
        Recruitment recruitment = recruitmentService.findRecruitment(targetId);
        RecruitmentFavorite favorite = RecruitmentFavorite.builder()
                .user(user)
                .recruitment(recruitment)
                .build();
        return recruitmentRepository.save(favorite);
    }

    @Override
    protected void delete(Favorite favorite) {
        recruitmentRepository.delete((RecruitmentFavorite) favorite);
    }

    @Override
    protected boolean exists(User user, Long targetId) {
        return recruitmentRepository.existsByUserAndRecruitment_Id(user, targetId);
    }

    @Override
    public RecruitmentFavorite getFavorite(Long id) {
        return recruitmentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorType.RECRUITMENT_FAVORITE_NOT_FOUND)
        );
    }

    @Override
    protected FavoriteDomain getDomain() {
        return FavoriteDomain.RECRUITMENT;
    }
}
