package com.example.sideproject.global.dto;

import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.enums.ResponseStatus;
import lombok.Getter;

@Getter
public class ResponseBooleanDto {

    private int status;
    private String message;
    private boolean result = false;

    public ResponseBooleanDto(ResponseStatus status, boolean result) {
        this.status = status.getHttpStatus().value();
        this.message = status.getMessage();
        this.result = result;
    }

    public ResponseBooleanDto(ErrorType errorType, boolean result) {
        this.status = errorType.getHttpStatus().value();
        this.message = errorType.getMessage();
        this.result = result;
    }

    public ResponseBooleanDto(ErrorType errorType){
        this.status = errorType.getHttpStatus().value();
        this.message = errorType.getMessage();
    }

}
