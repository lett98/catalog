package vn.ghtk.demo.catalog.application.port.in.mp;


import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductListCriteria;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductRepository;
import vn.ghtk.demo.catalog.common.PagedResult;
import vn.ghtk.demo.catalog.domain.category.CategoryId;
import vn.ghtk.demo.catalog.domain.mp.MasterProduct;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductStatus;

public class ListingMasterProductUC implements ListingMasterProduct {
    private final MasterProductRepository masterProductRepository;

    public ListingMasterProductUC(MasterProductRepository masterProductRepository) {
        this.masterProductRepository = masterProductRepository;
    }

    @Override
    public MasterProduct findProductById(MasterProductId masterProductId) {
        return masterProductRepository.findProductById(masterProductId);
    }

    @Override
    public PagedResult<MasterProduct> execute(MasterProductListQuery query) {
        MasterProductListCriteria criteria = new MasterProductListCriteria(query.page(),
                query.size(),
                query.sortFields(),
                query.categoryId() == null ? null : new CategoryId(query.categoryId()),
                query.status() == null ? null : MasterProductStatus.fromStatus(query.status()));
        return masterProductRepository.listProducts(criteria);
    }
}
