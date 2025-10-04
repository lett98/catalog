package vn.ghtk.demo.catalog.domain.common;

import vn.ghtk.demo.catalog.domain.event.DomainEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DomainEventCollector {
    private static final ThreadLocal<List<DomainEvent>> currentEvents = ThreadLocal.withInitial(ArrayList::new);

    public static List<DomainEvent> getEvents() {
        return new ArrayList((Collection)currentEvents.get());
    }

    private DomainEventCollector() {
    }

    public static void clear() {
        currentEvents.remove();
    }

    public static void addEvent(DomainEvent event) {
        ((List)currentEvents.get()).add(event);
    }
}
