package com.example.sideproject.domain.favorite;

import com.example.sideproject.domain.favorite.listener.FavoriteEvent;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import org.springframework.context.ApplicationEventPublisher;

public abstract class FavoriteService<T extends Favorite> {
    private final ApplicationEventPublisher publisher;

    public FavoriteService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public Long saveFavorite(User user, Long targetId) {
        if (exists(user, targetId)) {
            throw new CustomException(ErrorType.ALREADY_EXIST_FAVORITE);
        }
        T entity = save(user, targetId);
        publishAddEvent(entity.getTargetId());
        return entity.getTargetId();
    }

    public void deleteFavorite(User user, Long id) {
        T entity = getFavorite(id);
        if (!entity.isOwn(user.getId())) {
            throw new CustomException(ErrorType.NOT_OWNER);
        }
        delete(entity);
        publishRemoveEvent(entity.getTargetId());
    }

    /**
     * 관심 목록을 저장한다.
     * @param user 유저
     * @param targetId 관심 목록 연관 데이터의 고유번호
     * @return 저장된 관심 목록 데이터
     */
    protected abstract T save(User user, Long targetId);

    /**
     * 관심 목록을 삭제한다
     * @param favorite 유저
     */
    protected abstract void delete(Favorite favorite);

    protected abstract boolean exists(User user, Long targetId);
    public abstract T getFavorite(Long id);
    protected abstract FavoriteDomain getDomain();

    /**
     * 관심목록 카운트 +1 이벤트 발행
     * @param targetId 카운트가 있는 실 데이터 고유번호
     */
    protected void publishAddEvent(Long targetId) {
        publisher.publishEvent(new FavoriteEvent(targetId, FavoriteMode.ADD, getDomain()));
    }

    /**
     * 관심목록 카운트 -1 이벤트 발행
     * @param targetId 카운트가 있는 실 데이터 고유번호
     */
    protected void publishRemoveEvent(Long targetId) {
        publisher.publishEvent(new FavoriteEvent(targetId, FavoriteMode.REMOVE, getDomain()));
    }
}
