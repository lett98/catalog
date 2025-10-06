package vn.ghtk.demo.catalog.application.port.out.mp;

import vn.ghtk.demo.catalog.domain.BrandId;

public record SearchMasterProductCriteria(BrandId brandId,
                                          String model) {
}
