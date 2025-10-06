package vn.ghtk.demo.catalog.adapter.outbound.mp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.ghtk.demo.catalog.adapter.outbound.mp.jpa.repos.ProductJpaRepository;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductRepository;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductListCriteria;
import vn.ghtk.demo.catalog.application.port.out.mp.SearchMasterProductCriteria;
import vn.ghtk.demo.catalog.common.PagedResult;
import vn.ghtk.demo.catalog.domain.mp.MasterProduct;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

@Repository
@RequiredArgsConstructor
public class MasterProductOa implements MasterProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public MasterProduct findProductById(MasterProductId masterProductId) {
        return null;
    }

    @Override
    public PagedResult<MasterProduct> listProducts(MasterProductListCriteria criteria) {
        return null;
    }

    @Override
    public MasterProduct searchProduct(SearchMasterProductCriteria criteria) {
        return null;
    }

    @Override
    public void save(MasterProduct masterProduct) {

    }
}
