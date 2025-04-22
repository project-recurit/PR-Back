package com.example.sideproject.domain.pr.dto;

public enum PrSort {
    modifiedAt("modifiedAt"),
    createdAt("createdAt");

    PrSort(String order) {
        this.order = order;
    }

    final String order;

    public String getOrder() {
        return order;
    }
}
