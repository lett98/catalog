package vn.ghtk.demo.catalog.application.port.in.mp.input;

import vn.ghtk.demo.catalog.domain.BrandId;
import vn.ghtk.demo.catalog.domain.category.CategoryId;

public record CreateProductCmd(CategoryId categoryId,
                               BrandId brandId,
                               String model,
                               String title) {
}
