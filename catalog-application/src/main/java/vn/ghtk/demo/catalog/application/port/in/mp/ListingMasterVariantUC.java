package vn.ghtk.demo.catalog.application.port.in.mp;

import vn.ghtk.demo.catalog.application.port.out.mp.ListingMasterVariantOp;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariant;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;

import java.util.List;

public class ListingMasterVariantUC implements ListingMasterVariant {
    private final ListingMasterVariantOp listingMasterVariantOp;

    public ListingMasterVariantUC(ListingMasterVariantOp listingMasterVariantOp) {
        this.listingMasterVariantOp = listingMasterVariantOp;
    }

    @Override
    public MasterVariant findVariantById(MasterVariantId masterVariantId) {
        return listingMasterVariantOp.getMasterVariantById(masterVariantId);
    }

    @Override
    public List<MasterVariant> findVariantsOfProduct(MasterProductId masterProductId) {
        return listingMasterVariantOp.getProductVariants(masterProductId);
    }
}
