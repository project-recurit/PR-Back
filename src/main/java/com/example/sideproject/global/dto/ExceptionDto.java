package com.example.sideproject.global.dto;

import com.example.sideproject.global.enums.ErrorType;
import lombok.Getter;

@Getter
public class ExceptionDto {
    // 에러타입을 가지고 있는 데이터를 담고 있는 역할
    // jwtAuthenticationEntryPoint , jwtAuthenticationFilter, jwtExceptionFilter 에서 사용
    private String result;
    private ErrorType errorType;
    private String message;

    public ExceptionDto(ErrorType errorType) {
        this.result = "ERROR";
        this.errorType = errorType;
        this.message = errorType.getMessage();
    }

    public ExceptionDto(String message) {
        this.result = "ERROR";
        this.message = message;
    }
}