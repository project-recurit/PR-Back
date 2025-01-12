package com.example.sideproject.global.validation.techstack;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TechStackSizeValidator.class)
public @interface TechStackSize {
    int value() default 5;
    String message() default "기술 스택은 최대 {value}개까지 선택이 가능합니다.";
    Class[] groups() default {};
    Class[] payload() default {};
}
