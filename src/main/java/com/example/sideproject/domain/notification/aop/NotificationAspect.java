package com.example.sideproject.domain.notification.aop;

import com.example.sideproject.domain.notification.aop.annotation.NotifyOn;
import com.example.sideproject.domain.notification.dto.EventListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class NotificationAspect {
    private final ApplicationEventPublisher publisher;

    @AfterReturning(pointcut = "@annotation(annotation)", returning = "res")
    public void publish(EventListDto res, NotifyOn annotation) {
        log.info("이벤트 발행");
        publisher.publishEvent(res);
    }
}
