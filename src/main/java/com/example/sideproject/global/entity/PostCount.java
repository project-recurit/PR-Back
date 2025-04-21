package com.example.sideproject.global.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public final class PostCount {
    private int viewCount;
    private int commentCount;
    private int favoriteCount;

    public PostCount(int viewCount, int commentCount, int favoriteCount) {
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
    }

    public PostCount increaseViewCount() {
        this.viewCount++;
        return buildPostCount();
    }

    public PostCount decreaseViewCount() {
        if (this.viewCount > 0) {
            this.viewCount--;
        }
        return buildPostCount();
    }

    public PostCount increaseCommentCount() {
        this.commentCount++;
        return buildPostCount();
    }

    public PostCount decreaseCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
        return buildPostCount();
    }

    public PostCount increaseFavoriteCount() {
        this.favoriteCount++;
        return buildPostCount();
    }

    public PostCount decreaseFavoriteCount() {
        if (this.favoriteCount > 0) {
            this.favoriteCount--;
        }
        return buildPostCount();
    }

    public static PostCount init() {
        return new PostCount(0, 0, 0);
    }

    private PostCount buildPostCount() {
        return new PostCount(viewCount, commentCount, favoriteCount);
    }
}
