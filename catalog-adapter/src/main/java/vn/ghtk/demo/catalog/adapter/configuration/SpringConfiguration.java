package vn.ghtk.demo.catalog.adapter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ghtk.demo.catalog.application.port.in.mp.ListingMasterProduct;
import vn.ghtk.demo.catalog.application.port.in.mp.ListingMasterProductUC;
import vn.ghtk.demo.catalog.application.port.in.mp.ListingMasterVariant;
import vn.ghtk.demo.catalog.application.port.in.mp.ListingMasterVariantUC;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductRepository;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterVariantRepository;

@Configuration
public class SpringConfiguration {

    @Bean
    public ListingMasterProduct listingMasterProduct(MasterProductRepository masterProductRepository) {
        return new ListingMasterProductUC(masterProductRepository);
    }

    @Bean
    public ListingMasterVariant listingMasterVariant(MasterVariantRepository masterVariantRepository) {
        return new ListingMasterVariantUC(masterVariantRepository);
    }

//    @Bean
//    public ListingSku listingSku(SkuRepository skuRepository) {
//        return new ListingSkuUC(skuRepository);
//    }
}
