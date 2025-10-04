package vn.ghtk.demo.catalog.adapter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ghtk.demo.catalog.application.port.in.mp.*;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductRepository;
import vn.ghtk.demo.catalog.application.port.out.mp.ListingMasterVariantOp;

@Configuration
public class SpringConfiguration {

    @Bean
    public ListingMasterProduct listingMasterProduct(MasterProductRepository masterProductRepository) {
        return new ListingMasterProductUC(masterProductRepository);
    }

    @Bean
    public ListingMasterVariant listingMasterVariant(ListingMasterVariantOp listingMasterVariantOp) {
        return new ListingMasterVariantUC(listingMasterVariantOp);
    }

    @Bean
    public MasterVariantIp masterVariantIp(MasterProductRepository masterProductRepository) {
        return new MasterVariantUC(masterProductRepository);
    }

    @Bean
    public MasterProductIp masterProductIp(MasterProductRepository masterProductRepository) {
        return new MasterProductUC(masterProductRepository);
    }
}
