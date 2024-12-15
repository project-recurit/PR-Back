package com.example.sideproject.global.notification.service;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import com.example.sideproject.global.notification.dto.NotificationDto;
import com.example.sideproject.global.notification.dto.NotificationRequestDto;
import com.example.sideproject.global.notification.entity.Notification;
import com.example.sideproject.global.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationDto createNotification(NotificationRequestDto requestDto, User from) {
        Notification notification = requestDto.toEntity(from);
        notification = notificationRepository.save(notification);
        return NotificationDto.of(notification);
    }

    @Transactional
    public NotificationDto updateNotification(Long notificationId) {
        Notification notification = getNotification(notificationId);
        notification.read();
        return NotificationDto.of(notification);
    }

    public List<NotificationDto> getNotifications(User to) {
        List<Notification> notifications = notificationRepository.findByTo(to);
        List<NotificationDto> results = notifications.stream().map(NotificationDto::of).toList();
        return results;
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = getNotification(notificationId);
        notificationRepository.delete(notification);
    }

    private Notification getNotification(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorType.NOTIFICATION_NOT_FOUND));
    }
}
