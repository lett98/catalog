package vn.ghtk.demo.catalog.domain.mp;

public enum MasterProductStatus {
    DRAFT((short) 1),
    PUBLISHED((short) 2),
    RETIRED((short) 3);

    private short status;

    MasterProductStatus(short status) {
        this.status = status;
    }

    public short status() {
        return status;
    }

    public static MasterProductStatus fromStatus(Short status) {
        for (MasterProductStatus masterProductStatus : MasterProductStatus.values()) {
            if (masterProductStatus.status == status) {
                return masterProductStatus;
            }
        }
        return null;
    }
}
