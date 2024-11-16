package com.example.sideproject.global.dto;

import com.example.sideproject.global.enums.ErrorType;
import lombok.Getter;

@Getter
public class ResponseErrorMessageDto {
    private int status;
    private String message;

    public ResponseErrorMessageDto(ErrorType errorType){
        this.status = errorType.getHttpStatus().value();
        this.message = errorType.getMessage();
    }
}