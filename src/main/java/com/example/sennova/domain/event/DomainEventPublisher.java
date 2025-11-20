package com.example.sennova.domain.event;

public interface DomainEventPublisher {
    void publish(Object event);
}
