package com.example.sideproject.global.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Converter {
    private final ObjectMapper objectMapper;

    public Converter() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false);
    }

    public String toString(Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof String) {
            return (String) obj;  // String이면 그대로 반환
        }

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("[obj -> string] convert failed..");
            throw new RuntimeException("convert failed", e);
        }
    }
}
