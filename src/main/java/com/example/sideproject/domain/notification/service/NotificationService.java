package com.example.sideproject.domain.notification.service;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.dto.PageDto;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import com.example.sideproject.domain.notification.dto.NotificationDto;
import com.example.sideproject.domain.notification.dto.NotificationRequestDto;
import com.example.sideproject.domain.notification.entity.Notification;
import com.example.sideproject.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationDto createNotification(NotificationRequestDto requestDto) {
        Notification notification = requestDto.toEntity();
        notification = notificationRepository.save(notification);
        return NotificationDto.of(notification);
    }

    @Transactional
    public NotificationDto readNotification(Long notificationId, User user) {
        Notification notification = getNotification(notificationId);
        notification.read(user.getId());
        return NotificationDto.of(notification);
    }

    public PagedModel<NotificationDto> getNotifications(User to, PageDto pageDto) {
        PageRequest pageRequest = pageDto.toPageRequest();
        Page<Notification> notifications = notificationRepository.findByTo(to, pageRequest);

        Page<NotificationDto> results = notifications.map(NotificationDto::of);
        return new PagedModel<>(results);
    }

    public void deleteNotification(Long notificationId, User user) {
        Notification notification = getNotification(notificationId);
        notification.checkOwn(user.getId());
        notificationRepository.delete(notification);
    }

    private Notification getNotification(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorType.NOTIFICATION_NOT_FOUND));
    }
}
