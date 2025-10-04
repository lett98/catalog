package vn.ghtk.demo.catalog.domain.mp.exception;

import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;

public class NotFoundMVException extends RuntimeException {
    private MasterProductId masterProductId;
    private MasterVariantId masterVariantId;
    public NotFoundMVException(MasterProductId masterProductId, MasterVariantId masterVariantId) {
        super("MasterVariant [" + masterVariantId.id() + "] not found in MasterProduct [" + masterProductId.id() + "]");
        this.masterProductId = masterProductId;
        this.masterVariantId = masterVariantId;
    }
}
