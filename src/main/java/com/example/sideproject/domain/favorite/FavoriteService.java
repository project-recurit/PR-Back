package com.example.sideproject.domain.favorite;

import com.example.sideproject.domain.favorite.listener.FavoriteEvent;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.context.ApplicationEventPublisher;

public abstract class FavoriteService<T extends Favorite> {
    private final ApplicationEventPublisher publisher;

    public FavoriteService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public Long saveFavorite(User user, Long id) {
        T entity = save(user, id);
        publishAddEvent(entity.getTargetId());
        return entity.getTargetId();
    }

    public void deleteFavorite(User user, Long id) {
        Long typeId = delete(user, id);
        publishRemoveEvent(typeId);
    }

    /**
     * 관심 목록을 저장한다.
     * @param user 유저
     * @param targetId 관심 목록 연관 데이터의 고유번호
     * @return
     */
    protected abstract T save(User user, Long targetId);

    /**
     * 삭제 이후 targetId를 반환 한다.
     * @param user 유저
     * @param id 관심 목록 고유 번호
     * @return targetId
     */
    protected abstract Long delete(User user, Long id);
    public abstract T getFavorite(Long id);

    /**
     * 관심목록 카운트 +1 이벤트 발행
     * @param targetId 카운트가 있는 실 데이터 고유번호
     */
    protected void publishAddEvent(Long targetId) {
        publisher.publishEvent(new FavoriteEvent(targetId, FavoriteMode.ADD, FavoriteDomain.PR));
    }

    /**
     * 관심목록 카운트 -1 이벤트 발행
     * @param targetId 카운트가 있는 실 데이터 고유번호
     */
    protected void publishRemoveEvent(Long targetId) {
        publisher.publishEvent(new FavoriteEvent(targetId, FavoriteMode.REMOVE, FavoriteDomain.PR));
    }
}
