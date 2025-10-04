package vn.ghtk.demo.catalog.domain.event;

import java.time.LocalDateTime;

public abstract class AbstractDomainEvent implements DomainEvent {
    protected LocalDateTime createdAt;
    protected EventType eventType;

    public AbstractDomainEvent(EventType eventType) {
        this.eventType = eventType;
        this.createdAt = LocalDateTime.now();
    }
}
