package vn.ghtk.demo.catalog.domain.mp;

import vn.ghtk.demo.catalog.domain.common.DomainIdentity;

public class MasterVariantId extends DomainIdentity<Integer> {
    public MasterVariantId(Integer id) {
        super(id);
    }

    public MasterVariantId(Long id) {
        super(id.intValue());
    }
}
