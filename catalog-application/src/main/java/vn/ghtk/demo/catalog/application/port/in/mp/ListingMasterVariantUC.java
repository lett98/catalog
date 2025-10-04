package vn.ghtk.demo.catalog.application.port.in.mp;

import vn.ghtk.demo.catalog.application.port.out.mp.MasterVariantRepository;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariant;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;

import java.util.List;

public class ListingMasterVariantUC implements ListingMasterVariant {
    private final MasterVariantRepository masterVariantRepository;

    public ListingMasterVariantUC(MasterVariantRepository masterVariantRepository) {
        this.masterVariantRepository = masterVariantRepository;
    }

    @Override
    public MasterVariant findVariantById(MasterVariantId masterVariantId) {
        return masterVariantRepository.getMasterVariantById(masterVariantId);
    }

    @Override
    public List<MasterVariant> findVariantsOfProduct(MasterProductId masterProductId) {
        return masterVariantRepository.getProductVariants(masterProductId);
    }
}
