package com.example.sideproject.domain.notification.dto;

import com.example.sideproject.domain.notification.entity.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record EventDto(
        @Schema(description = "알림을 발생시킨 유저")
        Long to,
        // 본인에게 전송하지 않기 위함
        @JsonIgnore
        Long from,
        // 현재는 sse 연결 제외하고는 사용하지 않음
        @Schema(description = "알림 제목, sse 연결에서만 사용")
        String title,
        @Schema(description = "알림 내용")
        String msg,
        @Schema(description = "알림 타입")
        NotificationType type,
        @Schema(description = "연관된 id")
        Long relatedId,
        @Schema(description = "push 메시지 발송 여부")
        boolean pushAllowed,
        @Schema(description = "push 메시지 필요 알림 여부")
        boolean needToPush
) {
}
