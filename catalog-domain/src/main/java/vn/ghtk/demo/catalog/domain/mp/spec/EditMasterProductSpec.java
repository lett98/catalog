package vn.ghtk.demo.catalog.domain.mp.spec;

import vn.ghtk.demo.catalog.domain.attribute.AttributeConfig;

import java.util.List;

public record EditMasterProductSpec(String description,
                                    String title,
                                    List<AttributeConfig> attributeConfigs) {
}
