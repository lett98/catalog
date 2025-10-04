package vn.ghtk.demo.catalog.application.port.in.mp;

import vn.ghtk.demo.catalog.application.port.in.mp.input.AddMasterVariantCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterVariantCmd;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;

public interface MasterVariantIp {
    MasterVariantId createVariant(AddMasterVariantCmd cmd);
    void editVariant(EditMasterVariantCmd cmd);
    void publishVariant(MasterProductId masterProductId, MasterVariantId masterVariantId);
    void retireVariant(MasterProductId masterProductId, MasterVariantId masterVariantId);
}
