package vn.ghtk.demo.catalog.application.port.in.mp;

import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterProductCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.UpdateMPPriceCmd;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

public interface MasterProductIp {
    void editProduct(EditMasterProductCmd cmd);
    void updateBasePrice(UpdateMPPriceCmd cmd);
    void publishProduct(MasterProductId masterProductId);
    void retireProduct(MasterProductId masterProductId);

}
