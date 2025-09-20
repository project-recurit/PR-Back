package com.example.sideproject.fake;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

public class FakeApplicationEventPublisher implements ApplicationEventPublisher {
    private final List<Object> publishedEvents = new ArrayList<>();

    @Override
    public void publishEvent(ApplicationEvent event) {
        publishedEvents.add(event);
        System.out.println("published: " + event);
    }

    @Override
    public void publishEvent(Object event) {
        publishedEvents.add(event);
        System.out.println("published: " + event);
    }

    // 검증용 메서드들
    public List<Object> getPublishedEvents() {
        return new ArrayList<>(publishedEvents);
    }

    public int getEventCount() {
        return publishedEvents.size();
    }

    @SuppressWarnings("unchecked")
    public <T> T getLastEvent() {
        if (publishedEvents.isEmpty()) {
            return null;
        }
        return (T) publishedEvents.get(publishedEvents.size() - 1);
    }

    public void clear() {
        publishedEvents.clear();
    }
}
