package vn.ghtk.demo.catalog.domain.mp.exception;

import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;

public class PublishMasterVariantException extends RuntimeException {
    private MasterVariantId masterVariantId;
    public PublishMasterVariantException(MasterVariantId masterVariantId) {
        super("Can not publish master variant " + masterVariantId.id());
        this.masterVariantId = masterVariantId;
    }
}
