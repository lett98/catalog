package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response;

import lombok.Getter;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductStatus;

@Getter
public class MPOperationResult {
    private Integer masterProductId;
    private Short status;

    public MPOperationResult(MasterProductId masterProductId, MasterProductStatus status) {
        this.masterProductId = masterProductId.id();
        if(status != null){
            this.status = status.status();
        }
    }
}
