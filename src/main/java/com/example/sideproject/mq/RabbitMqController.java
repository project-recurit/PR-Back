package com.example.sideproject.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/mq")
public class RabbitMqController {
    private final RabbitMqService rabbitMqService;

    @PostMapping("/direct")
    public void sendDirectExchange(@RequestBody NotificationMessage notificationMessage) {
        rabbitMqService.send(notificationMessage);
    }
}
