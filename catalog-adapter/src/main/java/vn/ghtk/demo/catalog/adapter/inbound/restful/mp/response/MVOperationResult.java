package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response;

import lombok.Getter;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantStatus;

@Getter
public class MVOperationResult {
    private Integer masterVariantId;
    private Short status;

    public MVOperationResult(MasterVariantId masterVariantId, MasterVariantStatus status) {
        this.masterVariantId = masterVariantId.id();
        if(status != null){
            this.status = status.status();
        }
    }
}
