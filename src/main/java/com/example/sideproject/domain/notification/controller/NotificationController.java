package com.example.sideproject.domain.notification.controller;

import com.example.sideproject.domain.notification.dto.NotificationDto;
import com.example.sideproject.domain.notification.service.NotificationService;
import com.example.sideproject.global.dto.PageDto;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "알림 Rest API", description = "알림 데이터 조회, 삭제, 읽기 처리를 위한 API")
@RequestMapping("/api/v1/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "알림 데이터 조회", description = "알림 데이터 조회")
    @GetMapping
    public ResponseEntity<PagedModel<NotificationDto>> getNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                        PageDto pageDto) {
        return ResponseEntity.ok(notificationService.getNotifications(userDetails.getUser(), pageDto));
    }

    @Operation(summary = "알림 읽기 처리", description = "알림을 읽기 처리합니다.")
    @PutMapping("/{notificationId}")
    public ResponseEntity<NotificationDto> readNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable("notificationId") Long notificationId) {
        return ResponseEntity.ok(notificationService.readNotification(notificationId, userDetails.getUser()));
    }

    @Operation(summary = "알림 데이터 삭제", description = "알림 데이터를 삭제합니다.")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable("notificationId") Long notificationId) {
        notificationService.deleteNotification(notificationId, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
