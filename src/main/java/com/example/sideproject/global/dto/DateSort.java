package com.example.sideproject.global.dto;

import lombok.Getter;

@Getter
public enum DateSort {
    modifiedAt("modifiedAt"),
    createdAt("createdAt");

    DateSort(String order) {
        this.order = order;
    }

    final String order;

    public static DateSort basicSort() {
        return DateSort.modifiedAt;
    }
}
