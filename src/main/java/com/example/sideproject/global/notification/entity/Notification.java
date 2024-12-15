package com.example.sideproject.global.notification.entity;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    private User to;

    @ManyToOne
    private User from;

    private NotificationType type;
    private String message;
    private String relatedUrl;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isRead;

    @Builder
    public Notification(Long notificationId, User to, User from, NotificationType type, String message, String relatedUrl, boolean isRead) {
        this.notificationId = notificationId;
        this.to = to;
        this.from = from;
        this.type = type;
        this.message = message;
        this.relatedUrl = relatedUrl;
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
        if (this.from.getId() != userId) {
            throw new CustomException(ErrorType.NOTIFICATION_NOT_FOUND);
        }
    }
}
