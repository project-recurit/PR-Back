package com.example.sideproject.domain.project.entity;

public enum EstimatedDuration {
    WITHIN_THREE_MONTHS("3개월 이내"),
    THREE_MONTHS("3개월"),
    SIX_MONTHS("6개월"),
    NINE_MONTHS("9개월"),
    ONE_YEAR("1년"),
    MORE_THAN_ONE_YEAR("1년 이상");

    private final String description;

    EstimatedDuration(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
