package vn.ghtk.demo.catalog.application.port.in.mp;

import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariant;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;

import java.util.List;

public interface ListingMasterVariant {
    MasterVariant findVariantById(MasterVariantId masterVariantId);
    List<MasterVariant> findVariantsOfProduct(MasterProductId masterProductId);
}
