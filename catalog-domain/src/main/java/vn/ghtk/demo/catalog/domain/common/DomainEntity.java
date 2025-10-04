package vn.ghtk.demo.catalog.domain.common;

import java.util.Objects;

public class DomainEntity<T extends DomainIdentity<?>> {
    protected T id;

    public DomainEntity(T id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEntity<?> that = (DomainEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public T id() {
        return id;
    }
}
