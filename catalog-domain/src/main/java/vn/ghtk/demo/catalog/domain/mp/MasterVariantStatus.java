package vn.ghtk.demo.catalog.domain.mp;

public enum MasterVariantStatus {
    DRAFT((short) 1),
    PUBLISHED((short) 2),
    RETIRED((short) 3);

    private short status;

    MasterVariantStatus(short status) {
        this.status = status;
    }

    public short status() {
        return status;
    }
}
