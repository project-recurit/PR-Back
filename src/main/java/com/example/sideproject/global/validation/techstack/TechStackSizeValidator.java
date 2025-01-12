package com.example.sideproject.global.validation.techstack;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TechStackSizeValidator implements ConstraintValidator<TechStackSize, String> {

    private int size;

    @Override
    public void initialize(TechStackSize parameters) {
        this.size = parameters.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }

        String[] values = s.split(",");

        return values.length <= size;
    }
}
