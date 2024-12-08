package com.example.sideproject.global.dto;

import com.example.sideproject.global.enums.ResponseStatus;
import lombok.Getter;

@Getter
public class ResponseMessageDto {
    private int status;
    private String message;

    public ResponseMessageDto(ResponseStatus status) {
        this.status = status.getHttpStatus().value();
        this.message = status.getMessage();
    }

    //사용 예시
    //return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.COMMENT_DELETE_SUCCESS));
}