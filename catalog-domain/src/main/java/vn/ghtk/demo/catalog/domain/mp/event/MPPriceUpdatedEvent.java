package vn.ghtk.demo.catalog.domain.mp.event;

import vn.ghtk.demo.catalog.domain.Money;
import vn.ghtk.demo.catalog.domain.event.AbstractDomainEvent;
import vn.ghtk.demo.catalog.domain.event.EventType;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

public class MPPriceUpdatedEvent extends AbstractDomainEvent {
    private MasterProductId masterProductId;
    private Money newPrice;

    public MPPriceUpdatedEvent(MasterProductId masterProductId,
                               Money newPrice) {
        super(EventType.MASTER_PRODUCT_PRICE_UPDATED);
        this.masterProductId = masterProductId;
        this.newPrice = newPrice;
    }
}
