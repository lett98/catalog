package vn.ghtk.demo.catalog.adapter.facade.mp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterVariantDto;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterVariantListDto;
import vn.ghtk.demo.catalog.application.port.in.mp.ListingMasterVariant;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariant;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ViewMasterVariantFacade {
    private final ListingMasterVariant listingMasterVariant;

    public MasterVariantDto findMasterVariantById(Integer masterVariantId) {
        MasterVariant masterVariant = listingMasterVariant.findVariantById(new MasterVariantId(masterVariantId));
        return this.domainToDto(masterVariant);
    }

    public List<MasterVariantListDto> findVariantsOfProduct(Integer masterProductId) {
        List<MasterVariant> variants = listingMasterVariant.findVariantsOfProduct(new MasterProductId(masterProductId));
        return variants
                .stream()
                .map(this::domainToDtoList)
                .toList();
    }

    private MasterVariantListDto domainToDtoList(MasterVariant masterVariant) {
        //todo
        return null;
    }

    private MasterVariantDto domainToDto(MasterVariant masterVariant) {
        //todo
        return null;
    }
}
