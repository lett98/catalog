package vn.ghtk.demo.catalog.application.port.out;

import vn.ghtk.demo.catalog.domain.event.DomainEvent;

import java.util.List;

public interface PublishEventOp<T extends DomainEvent> {
    void publish(List<T> events);
}
