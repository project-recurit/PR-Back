package com.example.sideproject.domain.favorite.entity;

public enum FavoriteType {
    PROJECT("PROJECT"),
    RESUME("RESUME");

    private final String type;

    FavoriteType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
