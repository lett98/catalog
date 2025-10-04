package vn.ghtk.demo.catalog.domain.mp.exception;

import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

public class PublishMasterProductException extends RuntimeException {
    private MasterProductId masterProductId;
    public PublishMasterProductException(MasterProductId masterProductId) {
        super("Can not publish master product " + masterProductId.id());
        this.masterProductId = masterProductId;
    }
}
