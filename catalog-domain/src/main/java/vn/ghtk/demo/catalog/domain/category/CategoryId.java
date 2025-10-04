package vn.ghtk.demo.catalog.domain.category;

import vn.ghtk.demo.catalog.domain.common.DomainIdentity;

public class CategoryId extends DomainIdentity<Integer> {
    public CategoryId(Integer id) {
        super(id);
    }
}
