package com.example.sideproject.global.validation.time;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ValidLocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            LocalDateTime.parse(s, formatter);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
