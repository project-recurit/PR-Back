package com.example.sideproject.domain.applicant.entity;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
    unviewed("미열람", false),
    viewed("열람", false),
    rejected("거절", true),
    accepted("승인", true);

    ApplicationStatus(String description, boolean notify) {
        this.description = description;
        this.notify = notify;
    }

    final String description;
    final boolean notify;
}