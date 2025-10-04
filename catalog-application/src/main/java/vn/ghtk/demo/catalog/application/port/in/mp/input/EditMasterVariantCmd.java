package vn.ghtk.demo.catalog.application.port.in.mp.input;

import vn.ghtk.demo.catalog.domain.ImageUrl;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantName;
import vn.ghtk.demo.catalog.domain.mp.spec.EditProductVariantSpec;

import java.util.List;

public record EditMasterVariantCmd(MasterProductId masterProductId,
                                   MasterVariantId masterVariantId,
                                   List<ImageUrl> images,
                                   MasterVariantName name) {
    public EditProductVariantSpec buildEditProductVariantSpec() {
        return new EditProductVariantSpec(masterVariantId, images, name);
    }
}
