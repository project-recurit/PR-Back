package com.example.sideproject.domain.notification.entity;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User to;

    private NotificationType type;
    private String message;
    private Long relatedId;

    @ColumnDefault("false")
    private boolean isRead;

    @Builder
    public Notification(Long notificationId, User to, NotificationType type, String message, Long relatedId, boolean isRead) {
        this.notificationId = notificationId;
        this.to = to;
        this.type = type;
        this.message = message;
        this.relatedId = relatedId;
        this.isRead = isRead;
    }

    public void read(Long userId) {
        checkOwn(userId);
        read();
    }

    private void read() {
        this.isRead = true;
    }

    public void checkOwn(Long userId) {
        if (!Objects.equals(this.to.getId(), userId)) {
            throw new CustomException(ErrorType.NOTIFICATION_NOT_FOUND);
        }
    }
}
