package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.favorite.Favorite;
import com.example.sideproject.domain.favorite.FavoriteDomain;
import com.example.sideproject.domain.favorite.FavoriteService;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.PrFavorite;
import com.example.sideproject.domain.pr.repository.PrFavoriteRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PrFavoriteService extends FavoriteService<PrFavorite> {
    private final PrService prService;
    private final PrFavoriteRepository prFavoriteRepository;

    public PrFavoriteService(PrService prService, PrFavoriteRepository prFavoriteRepository, ApplicationEventPublisher publisher) {
        super(publisher);
        this.prService = prService;
        this.prFavoriteRepository = prFavoriteRepository;
    }

    @Override
    protected PrFavorite save(User user, Long targetId) {
        Pr pr = prService.getPr(targetId);
        PrFavorite favorite = PrFavorite.builder()
                .pr(pr)
                .user(user)
                .build();
        return prFavoriteRepository.save(favorite);
    }

    @Override
    protected void delete(Favorite favorite) {
        prFavoriteRepository.delete((PrFavorite) favorite);
    }

    @Override
    protected boolean exists(User user, Long targetId) {
        return prFavoriteRepository.existsByUserAndPr_Id(user, targetId);
    }

    @Override
    public PrFavorite getFavorite(Long id) {
        return prFavoriteRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorType.PR_FAVORITE_NOT_FOUND)
        );
    }

    @Override
    protected FavoriteDomain getDomain() {
        return FavoriteDomain.PR;
    }
}
