package vn.ghtk.demo.catalog.application.port.in.mp.input;

import vn.ghtk.demo.catalog.domain.attribute.AttributeConfig;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.spec.EditMasterProductSpec;

import java.util.List;

public record EditMasterProductCmd(MasterProductId masterProductId,
                                   String description,
                                   String title,
                                   List<AttributeConfig> attributeConfigs) {
    public EditMasterProductSpec buildProductSpec() {
        return new EditMasterProductSpec(description, title, attributeConfigs);
    }
}
