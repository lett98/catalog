package vn.ghtk.demo.catalog.application.port.out.mp;

import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariant;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;

import java.util.List;

public interface MasterVariantRepository {
    MasterVariant getMasterVariantById(MasterVariantId masterVariantId);
    List<MasterVariant> getProductVariants(MasterProductId masterProductId);
}
