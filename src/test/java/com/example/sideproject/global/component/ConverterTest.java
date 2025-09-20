package com.example.sideproject.global.component;

import com.example.sideproject.domain.notification.dto.recruitment.RecruitmentNotificationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {
    Converter converter;

    @BeforeEach
    void setUp() {
        converter = new Converter();
    }

    @DisplayName("문자열일 경우 해당 문자열을 반환한다.")
    @Test
    void testToString() {
        String str = "test 메시지";

        String result = converter.toString(str);

        assertThat(result).isEqualTo(str);
    }

    @DisplayName("객체일 경우 json 문자열을 반환한다.")
    @Test
    void testToString_Obj() {
        RecruitmentNotificationDto notificationDto = new RecruitmentNotificationDto(List.of("스택1", "스택2"), "등록 알림");

        String result = converter.toString(notificationDto);

        assertThat(result).contains("스택1", "스택2", "등록 알림");
    }
}