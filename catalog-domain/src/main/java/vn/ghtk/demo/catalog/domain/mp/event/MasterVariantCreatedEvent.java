package vn.ghtk.demo.catalog.domain.mp.event;

import vn.ghtk.demo.catalog.domain.event.AbstractDomainEvent;
import vn.ghtk.demo.catalog.domain.event.EventType;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;

public class MasterVariantCreatedEvent extends AbstractDomainEvent {
    private MasterProductId masterProductId;
    private MasterVariantId masterVariantId;

    public MasterVariantCreatedEvent(MasterProductId masterProductId,
                                     MasterVariantId masterVariantId) {
        super(EventType.MASTER_VARIANT_CREATED);
        this.masterProductId = masterProductId;
        this.masterVariantId = masterVariantId;
    }
}
