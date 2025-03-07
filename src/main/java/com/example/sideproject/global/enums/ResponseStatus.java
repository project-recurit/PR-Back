package com.example.sideproject.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    // User
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),
    USER_INFO_UPDATE(HttpStatus.OK, "추가 정보를 입력해주세요"),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하였습니다."),
    GET_USER_SUCCESS(HttpStatus.OK, "유저정보 조회에 성공하였습니다."),
    UNIQUE_ID(HttpStatus.OK, "사용할 수 있는 ID입니다."),
    UNIQUE_NICKNAME(HttpStatus.OK, "사용할 수 있는 닉네임입니다."),
    SUCCESS(HttpStatus.OK,"성공"),


    // 구독자 커뮤니티와 아티스트피드의 댓글
    CREATE_SUCCESS_COMMENT(HttpStatus.OK, "댓글이 생성되었습니다."),
    UPDATE_SUCCESS_COMMENT(HttpStatus.OK, "댓글이 수정되었습니다."),
    DELETE_SUCCESS_COMMENT(HttpStatus.OK, "댓글이 삭제되었습니다."),
    READ_SUCCESS_COMMENT(HttpStatus.OK, "댓글이 조회되었습니다."),
    // 좋아요
    SUCCESS_FEED_LIKE(HttpStatus.OK,"피드를 좋아합니다!" ),
    DELETE_FEED_LIKE(HttpStatus.OK, "피드의 좋아요를 취소합니다."),
    SUCCESS_COMMENT_LIKE(HttpStatus.OK,"댓글을 좋아합니다!" ),
    DELETE_COMMENT_LIKE(HttpStatus.OK, "댓글의 좋아요를 취소합니다."),

    //프로필
    WITHDRAW_SUCCESS(HttpStatus.OK, "회원탈퇴에 성공하였습니다."),
    SIGNUP_SUCCESS(HttpStatus.OK, "회원가입에 성공하였습니다."),
    PROFILE_UPDATE(HttpStatus.OK, "프로필이 변경되었습니다."),

    //RefreshToken
    UPDATE_TOKEN_SUCCESS_MESSAGE(HttpStatus.OK, "토큰이 재발급되었습니다."),

    SUCCESS_ACTIVATE_USER(HttpStatus.OK, "계정이 활성화되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 되지 않았습니다." ),
    CREATE_TEAM_RECRUIT_SUCCESS(HttpStatus.OK, "팀 모집 게시글이 생성되었습니다."),

    UPDATE_TEAM_RECRUIT_SUCCESS(HttpStatus.OK, "팀 모집 게시글이 수정되었습니다."),

    GET_TEAM_RECRUIT_SUCCESS(HttpStatus.OK, "팀 모집 게시글이 조회되었습니다."),
    DELETE_TEAM_RECRUIT_SUCCESS(HttpStatus.OK, "팀 모집 게시글이 삭제되었습니다."),
    CREATE_PROJECT_RECRUIT_SUCCESS(HttpStatus.OK, "프로젝트 모집 게시글이 생성되었습니다."),
    UPDATE_PROJECT_RECRUIT_SUCCESS(HttpStatus.OK, "프로젝트 모집 게시글이 수정되었습니다."),
    GET_PROJECT_RECRUIT_SUCCESS(HttpStatus.OK, "프로젝트 모집 게시글이 조회되었습니다."), 
    DELETE_PROJECT_RECRUIT_SUCCESS(HttpStatus.OK, "프로젝트 모집 게시글이 삭제되었습니다."),
    GET_BOOKMARKS_SUCCESS(HttpStatus.OK, "북마크 조회에 성공하였습니다."),
    BOOKMARK_SUCCESS(HttpStatus.OK, "북마크 상태가 변경되었습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}