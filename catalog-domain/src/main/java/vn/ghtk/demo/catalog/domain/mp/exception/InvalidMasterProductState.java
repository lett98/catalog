package vn.ghtk.demo.catalog.domain.mp.exception;

import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductStatus;

public class InvalidMasterProductState extends RuntimeException {
    private MasterProductId masterProductId;
    private MasterProductStatus status;
    public InvalidMasterProductState(MasterProductId masterProductId, MasterProductStatus status) {
        super(String.format("MasterProduct: %s is in status %s", masterProductId.id(), status));
        this.masterProductId = masterProductId;
    }

    public MasterProductId masterProductId() {
        return masterProductId;
    }
}
