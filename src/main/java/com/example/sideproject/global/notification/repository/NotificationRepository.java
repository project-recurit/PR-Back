package com.example.sideproject.global.notification.repository;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByTo(User to);
}
