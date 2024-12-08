package com.example.sideproject.global.auth.dto;

import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.global.enums.ErrorType;
import lombok.Getter;

@Getter
public class ForbiddenResponseDto {
    private UserStatus statusEnum;
    private int status;
    private String message;

    public ForbiddenResponseDto(UserStatus statusEnum, ErrorType errorType) {
        this.statusEnum = statusEnum;
        this.status = errorType.getHttpStatus().value();
        this.message = errorType.getMessage();
    }



}