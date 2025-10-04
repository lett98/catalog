package vn.ghtk.demo.catalog.application.port.out.mp;

import vn.ghtk.demo.catalog.domain.category.CategoryId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductStatus;

import java.util.List;

public record MasterProductListCriteria(int page,
                                        int size,
                                        List<String> sortFields,
                                        CategoryId categoryId,
                                        MasterProductStatus status) {
}
