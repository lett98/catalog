package vn.ghtk.demo.catalog.domain.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import vn.ghtk.demo.catalog.domain.common.DomainEventCollector;
import vn.ghtk.demo.catalog.domain.event.DomainEvent;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.event.MasterProductCreatedEvent;
import vn.ghtk.demo.catalog.domain.mp.event.MasterProductPublishedEvent;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class DomainEventCollectorTest {

    @AfterEach
    void tearDown() {
        DomainEventCollector.clear();
    }

    @Test
    void addEvent_shouldAddEventToCollector() {
        // Given
        DomainEvent event = new MasterProductCreatedEvent(new MasterProductId(1));

        // When
        DomainEventCollector.addEvent(event);

        // Then
        List<DomainEvent> events = DomainEventCollector.getEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof MasterProductCreatedEvent);
    }

    @Test
    void addEvent_shouldAddMultipleEvents() {
        // Given
        DomainEvent event1 = new MasterProductCreatedEvent(new MasterProductId(1));
        DomainEvent event2 = new MasterProductPublishedEvent(new MasterProductId(1));

        // When
        DomainEventCollector.addEvent(event1);
        DomainEventCollector.addEvent(event2);

        // Then
        List<DomainEvent> events = DomainEventCollector.getEvents();
        assertEquals(2, events.size());
    }

    @Test
    void clear_shouldRemoveAllEvents() {
        // Given
        DomainEventCollector.addEvent(new MasterProductCreatedEvent(new MasterProductId(1)));
        DomainEventCollector.addEvent(new MasterProductPublishedEvent(new MasterProductId(1)));

        // When
        DomainEventCollector.clear();

        // Then
        List<DomainEvent> events = DomainEventCollector.getEvents();
        assertEquals(0, events.size());
    }

    @Test
    void getEvents_shouldReturnCopyOfEvents() {
        // Given
        DomainEvent event = new MasterProductCreatedEvent(new MasterProductId(1));
        DomainEventCollector.addEvent(event);

        // When
        List<DomainEvent> events1 = DomainEventCollector.getEvents();
        List<DomainEvent> events2 = DomainEventCollector.getEvents();

        // Then
        assertNotSame(events1, events2, "Should return different list instances");
        assertEquals(events1.size(), events2.size(), "Should have same content");
    }

    @Test
    void getEvents_shouldNotAffectInternalStateWhenModified() {
        // Given
        DomainEvent event = new MasterProductCreatedEvent(new MasterProductId(1));
        DomainEventCollector.addEvent(event);

        // When
        List<DomainEvent> events = DomainEventCollector.getEvents();
        events.clear(); // Modify the returned list

        // Then
        List<DomainEvent> eventsAfter = DomainEventCollector.getEvents();
        assertEquals(1, eventsAfter.size(), "Internal state should not be affected");
    }

    @Test
    void threadLocal_shouldIsolateEventsBetweenThreads() throws InterruptedException {
        // Given
        CountDownLatch latch = new CountDownLatch(2);
        AtomicReference<List<DomainEvent>> thread1Events = new AtomicReference<>();
        AtomicReference<List<DomainEvent>> thread2Events = new AtomicReference<>();

        // When
        Thread thread1 = new Thread(() -> {
            DomainEventCollector.addEvent(new MasterProductCreatedEvent(new MasterProductId(1)));
            thread1Events.set(DomainEventCollector.getEvents());
            latch.countDown();
        });

        Thread thread2 = new Thread(() -> {
            DomainEventCollector.addEvent(new MasterProductPublishedEvent(new MasterProductId(2)));
            thread2Events.set(DomainEventCollector.getEvents());
            latch.countDown();
        });

        thread1.start();
        thread2.start();
        latch.await();

        // Then
        assertEquals(1, thread1Events.get().size());
        assertEquals(1, thread2Events.get().size());
        assertTrue(thread1Events.get().get(0) instanceof MasterProductCreatedEvent);
        assertTrue(thread2Events.get().get(0) instanceof MasterProductPublishedEvent);
    }

    @Test
    void getEvents_shouldReturnEmptyListInitially() {
        // When
        List<DomainEvent> events = DomainEventCollector.getEvents();

        // Then
        assertNotNull(events);
        assertEquals(0, events.size());
    }
}
