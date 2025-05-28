package com.example.sideproject.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/push")
public class PushController {
    private final FcmNotificationSender fcmNotificationSender;

    @PostMapping("/direct")
    public void sendDirectExchange(@RequestBody NotificationMessage notificationMessage) {
        fcmNotificationSender.send(notificationMessage);
    }
}
