package com.example.sideproject.domain.notification.dto.recruitment;

import java.util.List;

public record RecruitmentNotificationDto(
        List<String> techStack,
        String title
) {
}
