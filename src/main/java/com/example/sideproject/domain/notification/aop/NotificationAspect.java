package com.example.sideproject.domain.notification.aop;

import com.example.sideproject.domain.notification.aop.annotation.NotifyOn;
import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class NotificationAspect {
    private final ApplicationEventPublisher publisher;

    @AfterReturning(pointcut = "@annotation(annotation)", returning = "res")
    public void publish(EventListDto res, NotifyOn annotation) {
        EventListDto filtered = filterEvents(res);
        if (!filtered.eventDtos().isEmpty()) {
            log.info("이벤트 발행: {}건", filtered.eventDtos().size());
            publisher.publishEvent(filtered);
        }
    }

    /**
     * 본인에게는 알림이 안가도록 필터링
     * @param listDto 알림 리스트
     * @return 본인 제외한 알림 리스트
     */
    private EventListDto filterEvents(EventListDto listDto) {
        List<EventDto> filtered = listDto.eventDtos().stream()
                .filter(event -> !Objects.equals(event.from(), event.to()))
                .toList();
        return new EventListDto(filtered);
    }
}
