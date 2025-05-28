package com.example.sideproject.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class FcmNotificationSender {
    private final RabbitTemplate rabbitTemplate;
    private final String DIRECT_EXCHANGE_NAME = "fcm";
    private final String DIRECT_QUEUE_ROUTING_KEY = "fcm.notification";

    public void send(NotificationMessage notificationMessage) {
        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE_NAME, DIRECT_QUEUE_ROUTING_KEY, notificationMessage);
    }
}
