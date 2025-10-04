package vn.ghtk.demo.catalog.domain.mp.spec;

import vn.ghtk.demo.catalog.domain.ImageUrl;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantName;

import java.util.List;

public record EditProductVariantSpec(MasterVariantId masterVariantId,
                                     List<ImageUrl> images,
                                     MasterVariantName name) {
}
