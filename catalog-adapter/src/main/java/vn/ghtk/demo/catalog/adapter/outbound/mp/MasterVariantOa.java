package vn.ghtk.demo.catalog.adapter.outbound.mp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.ghtk.demo.catalog.adapter.outbound.mp.jpa.repos.VariantJpaRepository;
import vn.ghtk.demo.catalog.application.port.out.mp.ListingMasterVariantOp;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariant;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MasterVariantOa implements ListingMasterVariantOp {
    private final VariantJpaRepository variantJpaRepository;

    @Override
    public MasterVariant getMasterVariantById(MasterVariantId masterVariantId) {
        return null;
    }

    @Override
    public List<MasterVariant> getProductVariants(MasterProductId masterProductId) {
        return List.of();
    }
}
