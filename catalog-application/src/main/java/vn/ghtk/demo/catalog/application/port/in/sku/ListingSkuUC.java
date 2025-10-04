//package vn.ghtk.demo.catalog.application.port.in.sku;
//
//import vn.ghtk.demo.catalog.application.port.out.sku.SkuListCriteria;
//import vn.ghtk.demo.catalog.application.port.out.sku.SkuRepository;
//import vn.ghtk.demo.catalog.common.PagedResult;
//import vn.ghtk.demo.catalog.domain.sku.Sku;
//import vn.ghtk.demo.catalog.domain.sku.SkuId;
//
//public class ListingSkuUC implements ListingSku {
//    private final SkuRepository skuRepository;
//
//    public ListingSkuUC(SkuRepository skuRepository) {
//        this.skuRepository = skuRepository;
//    }
//
//    @Override
//    public Sku findById(SkuId id) {
//        return skuRepository.getSkuById(id);
//    }
//
//    @Override
//    public PagedResult<Sku> execute(SkuListQuery query) {
//        SkuListCriteria criteria  = new SkuListCriteria(query.merchantId(), query.page(), query.size(), query.sortFields());
//        return skuRepository.listSkus(criteria);
//    }
//}
