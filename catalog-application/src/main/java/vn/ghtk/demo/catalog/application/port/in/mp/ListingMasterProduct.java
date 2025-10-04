package vn.ghtk.demo.catalog.application.port.in.mp;

import vn.ghtk.demo.catalog.common.PagedResult;
import vn.ghtk.demo.catalog.domain.mp.MasterProduct;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

public interface ListingMasterProduct {
    MasterProduct findProductById(MasterProductId masterProductId);
    PagedResult<MasterProduct> execute(MasterProductListQuery query);

}
