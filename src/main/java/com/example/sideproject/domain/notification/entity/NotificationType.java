package com.example.sideproject.domain.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    CONNECT("Success", false),
    PROJECT_REGISTRATION("\'%s\'이(가) 등록되었습니다.", true), // 프로젝트 제목
    PROJECT_APPLICANT("\'%s\'의 \'%s\'에 새로운 지원자가 있어요!", true),   // 프로젝트 제목, 포지션
    APPLICATION_RESULT("\'$s\'에 \'%s\'됐어요.", true),   // 프로젝트 제목, push 여부는 ApplicationStatus를 확인
    CHAT_START("\'%s\'님과 새로운 채팅이 시작됐어요.", true),    // 채팅 생성자
    ;

    private final String message;
    private final boolean needToPush;
}
