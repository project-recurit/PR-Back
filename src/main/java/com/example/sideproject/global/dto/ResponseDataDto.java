package com.example.sideproject.global.dto;

import com.example.sideproject.global.enums.ResponseStatus;
import lombok.Getter;

@Getter
public class ResponseDataDto<T> {
    private ResponseStatus status;
    private String message;
    private T data;

    public ResponseDataDto(ResponseStatus responseStatus, T data) {
        this.status = responseStatus;
        this.message = responseStatus.getMessage();
        this.data = data;
        //상태를 담아주기
    }

    // 사용 예시
    // return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.CARD_UPDATE_SUCCESS, responseDto));
}
