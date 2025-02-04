package com.example.sideproject.domain.project.entity;

public enum RecruitStatus {

    IN_PROGRESS("진행중"),
    CLOSED("마감");
    private final String description;

    RecruitStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
