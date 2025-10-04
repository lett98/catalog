package vn.ghtk.demo.catalog.domain.mp.exception;

import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

public class NotFoundMPException extends RuntimeException {
    private MasterProductId masterProductId;
    public NotFoundMPException(MasterProductId masterProductId) {
        super("Not found MasterProduct [" + masterProductId.id()+ "]");
        this.masterProductId = masterProductId;
    }
}
