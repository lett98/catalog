package vn.ghtk.demo.catalog.domain.mp;

import vn.ghtk.demo.catalog.domain.BrandId;

public record MasterProductTitle(String title,
                                 String model,
                                 BrandId brandId) {
}
