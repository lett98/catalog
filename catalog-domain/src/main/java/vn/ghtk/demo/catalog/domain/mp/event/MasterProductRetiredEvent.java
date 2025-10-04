package vn.ghtk.demo.catalog.domain.mp.event;

import vn.ghtk.demo.catalog.domain.event.AbstractDomainEvent;
import vn.ghtk.demo.catalog.domain.event.EventType;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

public class MasterProductRetiredEvent extends AbstractDomainEvent {
    private MasterProductId masterProductId;

    public MasterProductRetiredEvent(MasterProductId masterProductId) {
        super(EventType.MASTER_PRODUCT_RETIRED);
        this.masterProductId = masterProductId;
    }
}
