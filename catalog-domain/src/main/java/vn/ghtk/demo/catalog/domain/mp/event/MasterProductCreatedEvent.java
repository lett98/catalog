package vn.ghtk.demo.catalog.domain.mp.event;

import vn.ghtk.demo.catalog.domain.event.AbstractDomainEvent;
import vn.ghtk.demo.catalog.domain.event.EventType;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

public class MasterProductCreatedEvent extends AbstractDomainEvent {
    private MasterProductId masterProductId;

    public MasterProductCreatedEvent(MasterProductId masterProductId) {
        super(EventType.MASTER_PRODUCT_CREATED);
        this.masterProductId = masterProductId;
    }
}
