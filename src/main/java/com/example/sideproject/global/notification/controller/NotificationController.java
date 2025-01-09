package com.example.sideproject.global.notification.controller;

import com.example.sideproject.global.notification.dto.NotificationDto;
import com.example.sideproject.global.notification.service.NotificationService;
import com.example.sideproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.getNotifications(userDetails.getUser()));
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<NotificationDto> readNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable("notificationId") Long notificationId) {
        return ResponseEntity.ok(notificationService.readNotification(notificationId, userDetails.getUser()));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable("notificationId") Long notificationId) {
        notificationService.deleteNotification(notificationId, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
