package com.example.sideproject.domain.notification.repository;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByTo(User to, Pageable pageable);
}
