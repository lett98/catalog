package vn.ghtk.demo.catalog.application.port.out.mp;

import vn.ghtk.demo.catalog.common.PagedResult;
import vn.ghtk.demo.catalog.domain.mp.MasterProduct;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

public interface MasterProductRepository {
    MasterProduct findProductById(MasterProductId masterProductId);
    PagedResult<MasterProduct> listProducts(MasterProductListCriteria productListCriteria);

    void save(MasterProduct masterProduct);

}
