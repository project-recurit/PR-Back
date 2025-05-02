package com.example.sideproject.domain.recruitment.entity;

public enum RecruitmentCategory {
    ECOMMERCE("이커머스"),
    FINANCE("금융"),
    COMMUNITY("커뮤니티"),
    SOCIAL_MEDIA("소셜미디어"),
    TELECOMMUNICATIONS("통신 / 네트워크"),
    EDUCATION("교육"),
    PUBLIC_SECTOR("공공"),
    HEALTHCARE("의료 / 헬스케어"),
    MANUFACTURING("제조"),
    HARDWARE_EMBEDDED("하드웨어 / 임베디드"),
    GAME("게임"),
    SECURITY_ANTIVIRUS("보안 / 백신"),
    AI("AI");

    private final String description;

    RecruitmentCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}