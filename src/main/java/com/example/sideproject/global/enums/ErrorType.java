package com.example.sideproject.global.enums;

import com.example.sideproject.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType implements ErrorCode {
    /*    사용예시
    //user
    DUPLICATE_EMAIL(HttpStatus.LOCKED, "이미 존재하는 이메일입니다."),
    DEACTIVATE_USER(HttpStatus.LOCKED, "탈퇴한 회원입니다."),
    INVALID_PASSWORD(HttpStatus.LOCKED, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.LOCKED, "존재하지 않는 회원입니다."),
    NOT_AVAILABLE_PERMISSION(HttpStatus.LOCKED, "권한이 없습니다.")
     */

    // common
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "255자 이내로 입력해주세요."),
    TOKEN_ERROR(HttpStatus.UNAUTHORIZED,
            "토큰 에러!"),

    // like
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요가 없습니다."),
    SELF_LIKE(HttpStatus.NOT_ACCEPTABLE, "자신의 글/댓글에 좋아요 사용 불가"),
    DUPLICATE_LIKE(HttpStatus.NOT_ACCEPTABLE, "좋아요는 한번만 가능합니다."),
    USER_MISMATCH(HttpStatus.NOT_ACCEPTABLE, "유저와 좋아요가 일치하지 않습니다."),
    CONTENT_TYPE_MISMATCH(HttpStatus.NOT_ACCEPTABLE, "컨텐츠 타입이 일치하지 않습니다."),
    NOT_AVAILABLE_PERMISSION(HttpStatus.LOCKED, "권한이 없습니다."),
    NOT_SUBSCRIBED_ARTIST_GROUP(HttpStatus.NOT_FOUND, "구독하지 않은 그룹입니다."),

    //user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    MISMATCH_PASSWORD(HttpStatus.NOT_ACCEPTABLE, "비밀번호가 일치하지 않습니다."),
    DUPLICATE_ID(HttpStatus.OK, "중복된 아이디입니다."),
    BLACKLIST_EMAIL(HttpStatus.OK, "블랙리스트에 등록된 이메일로 가입할 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.OK, "중복된 닉네임입니다."),
    WITHDRAW_USER(HttpStatus.NOT_FOUND, "탈퇴한 회원입니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.NOT_FOUND, "REFRESH_TOKEN 값이 일치 하지 않습니다."),
    MISMATCH_ADMINTOKEN(HttpStatus.OK, "Admin 토큰값이 일치하지 않습니다."),
    MISMATCH_ARTISTTOKEN(HttpStatus.OK, "Artist 토큰값이 일치하지 않습니다."),
    MISMATCH_ENTERTAINMENTTOKEN(HttpStatus.OK, "Entertainment 토큰값이 일치하지 않습니다."),
    AUTH_NUM_NOTFOUND(HttpStatus.NOT_FOUND,"인증번호가 없습니다 확인부탁드립니다."),
    AUTH_MISMATCH(HttpStatus.NOT_FOUND,"인증번호가 틀렸습니다. 확인부탁드립니다."),
    EMAIL_DUPLICATE(HttpStatus.NOT_FOUND,"이메일 중복"),
    NOT_AUTH_EMAIL(HttpStatus.OK, "이메일이 인증되지 않았습니다."),

    //Feed
    FEED_NOT_FOUND(HttpStatus.NOT_FOUND,"찾는 피드가 없습니다."),
    SUBSCRIPT_FEED_NOT_FOUND(HttpStatus.NOT_FOUND,"구독한 그룹에 피드가 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"접근 권한이 없습니다."),

    // Entertainment
    NOT_FOUND_ENTER(HttpStatus.NOT_FOUND, "존재하지 않는 엔터테인먼트 계정입니다."),
    ALREADY_EXIST_ENTER_NAME(HttpStatus.NOT_FOUND, "이미 존재하는 엔터테인먼트 이름입니다."),
    ALREADY_EXIST_ENTER_NUMBER(HttpStatus.NOT_FOUND, "이미 존재하는 사업자번호입니다."),

    // 엔터테인먼트 및 아티스트 관련 에러
    ENTERTAINMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "엔터테인먼트를 찾을 수 없습니다."),
    ARTIST_NOT_FOUND(HttpStatus.NOT_FOUND, "아티스트를 찾을 수 없습니다."),
    ARTIST_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "아티스트 그룹을 찾을 수 없습니다."),
    ENROLLED_USER_AS_ARTIST(HttpStatus.UNAUTHORIZED, "이미 아티스트 등록을 하셨습니다."),
    ENROLLED_ARTIST_NAME(HttpStatus.UNAUTHORIZED, "이미 아티스트 등록을 하셨습니다."),
    NOT_FOUND_SUBSCRIBER(HttpStatus.NOT_FOUND, "구독자를 찾을 수 없습니다."),

    //유저 커뮤니티 피드
    NOT_FOUND_FEED(HttpStatus.NOT_FOUND, "피드를 찾을수 없습니다"),
    NOT_USER_FEED(HttpStatus.NOT_ACCEPTABLE, "유저가 작성한 피드가 아닙니다."),
    NOT_MATCH_USER(HttpStatus.NOT_ACCEPTABLE, "해당 유저는 아티스트그룹을 구독하지 않았습니다."),
    UNAUTHORIZED_FEED_ACCESS(HttpStatus.FORBIDDEN, "유저 커뮤니티에 접근할 권한이 없습니다."),
    UNAUTHORIZED_FEED_CREATE(HttpStatus.FORBIDDEN, "유저 커뮤니티 피드를 작성할 권한이 없습니다."),
    UNAUTHORIZED_FEED_DELETE(HttpStatus.FORBIDDEN, "유저 커뮤니티 피드를 삭제할 권한이 없습니다."),

    // 유저 커뮤니티 & 아티스트 피드 댓글
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글을 찾지 못했습니다."),
    NOT_USER_COMMENT(HttpStatus.NOT_ACCEPTABLE, "유저가 작성한 댓글이 아닙니다."),
    DUPLICATE_COMMENT(HttpStatus.LOCKED, "같은 댓글은 작성하실수 없습니다"),

    // 구독
    DUPLICATE_USER(HttpStatus.LOCKED, "이미 구독한 그룹입니다."),
    NOT_SUBSCRIPT_USER(HttpStatus.LOCKED, "구독한 그룹이 아닙니다."),
    NOT_FOUND_SUBSCRIPT_USER(HttpStatus.NOT_FOUND, "구독자를 찾을수 없습니다."),
    NOT_FOUND_SUBSCRIPTION(HttpStatus.NOT_FOUND, "구독한 그룹이 없습니다."),

    //좋아요
    NOT_FOUND_FEED_LIKE(HttpStatus.NOT_FOUND, "좋아요한 피드가 아닙니다"),
    NOT_FOUND_FEED_LIKES(HttpStatus.NOT_FOUND, "좋아요한 피드가 없습니다."),
    NOT_FOUND_COMMENT_LIKE(HttpStatus.NOT_FOUND, "좋아요한 댓글이 아닙니다"),
    NOT_FOUND_LIKE_USER(HttpStatus.NOT_FOUND, "좋아요를 누른 유저가 없습니다."),

    //아티스트그룹
    NOT_FOUND_ARTISTGROUP(HttpStatus.NOT_FOUND, "아티스트그룹을 찾지 못했습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    DUPLICATE_GROUP_NAME(HttpStatus.BAD_REQUEST, "그룹 이름이 중복되었습니다."),
    ARTIST_NOT_IN_GROUP(HttpStatus.BAD_REQUEST, "아티스트가 그룹에 없습니다."),

    //엔터피드
    ENTER_FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "피드를 찾을 수 없습니다."),

    // 상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),

    //S3
    NOT_ALLOWED_EXTENSION(HttpStatus.BAD_REQUEST, "업로드할 수 없는 확장자 파일입니다."),
    UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "업로드중 오류가 발생했습니다."),
    DELETE_ERROR(HttpStatus.BAD_REQUEST, "파일 삭제중 오류가 발생했습니다."),
    NOT_IMAGE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "이미지 파일이 아닙니다."),
    OVER_LOAD(HttpStatus.NOT_ACCEPTABLE, "사진은 10MB를 초과할 수 없습니다."),
    MAX_IMAGES_EXCEEDED(HttpStatus.NOT_ACCEPTABLE, "사진은 최대 10장 업로드할 수 있습니다."),
    NOT_STORED_FILE_NAME(HttpStatus.BAD_REQUEST, "해당 이름을 가진 파일이 존재하지 않습니다."),

    INACTIVE_USER(HttpStatus.FORBIDDEN, "휴면 계정입니다. 휴면 상태를 풀어주세요."),
    CHECK_YOUR_INFO(HttpStatus.NOT_FOUND, "입력한 정보의 유저를 찾을 수 없습니다."),
    NOT_YOUR_ENTERTAINMENT(HttpStatus.NOT_ACCEPTABLE, "소속사가 다른 아티스트입니다."),
    ARTIST_NOT_SUBSCRIBED(HttpStatus.NOT_ACCEPTABLE, "구독한 뒤 사용할 수 있는 서비스 입니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    TOKEN_INVALIDATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 무효화 중 오류가 발생했습니다."),
    TOKEN_REFRESH_FAILED(HttpStatus.BAD_REQUEST, "토큰 갱신에 실패했습니다."),
    LOGOUT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "로그아웃 처리 중 오류가 발생했습니다."), 
    TEAM_RECRUIT_NOT_FOUND(HttpStatus.NOT_FOUND, "팀 모집글을 찾을 수 없습니다."), 
    NOT_YOUR_POST(HttpStatus.NOT_ACCEPTABLE, "작성자가 아닙니다."),

    PROJECT_RECRUIT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트 구인 게시글입니다."),



    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
