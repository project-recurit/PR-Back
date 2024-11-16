package com.example.sideproject.global.exception;

import com.example.sideproject.global.enums.ErrorType;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorType errorType;

    public CustomException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    //사용 예시
    // if(user.isEmpty()){
    //    throw new CustomException(ErrorType.NOT_FOUND_USER);
    // }
}
