package vn.ghtk.demo.catalog.adapter.facade.mp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterProductDto;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterProductListDto;
import vn.ghtk.demo.catalog.application.port.in.mp.ListingMasterProduct;
import vn.ghtk.demo.catalog.application.port.in.mp.input.MasterProductListQuery;
import vn.ghtk.demo.catalog.common.PagedResult;
import vn.ghtk.demo.catalog.domain.mp.MasterProduct;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ViewMasterProductFacade {
    private final ListingMasterProduct listingMasterProduct;

    public MasterProductDto findMasterProductById(Integer masterProductId) {
        MasterProduct masterProduct = listingMasterProduct.findProductById(new MasterProductId(masterProductId));
        return this.domainToDto(masterProduct);
    }

    public PagedResult<MasterProductListDto> listMasterProducts(int page, int size, List<String> sortFields, Integer categoryId, Short status) {
        MasterProductListQuery query = new MasterProductListQuery(page, size, sortFields, categoryId, status);
        PagedResult<MasterProduct> pagedProducts = listingMasterProduct.execute(query);
        List<MasterProductListDto> productListDto = pagedProducts
                .detail()
                .stream()
                .map(this::domainToDtoList)
                .toList();
        return new PagedResult<>(productListDto, pagedProducts.total());
    }

    private MasterProductListDto domainToDtoList(MasterProduct masterProduct) {
        //todo
        return null;
    }

    private MasterProductDto domainToDto(MasterProduct masterProduct) {
        //todo
        return null;
    }


}
