package com.example.sideproject.global.exception;

import lombok.Getter;
import org.springframework.security.authentication.DisabledException;

@Getter
public class InactiveException extends DisabledException {

    public InactiveException(String msg) {
        super(msg);
    }
}