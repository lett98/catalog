package vn.ghtk.demo.catalog.domain.common;

import java.util.Objects;

public abstract class DomainIdentity<T> {
    protected T id;

    public DomainIdentity(T id) {
        this.id = id;
    }

    public T id() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainIdentity<?> that = (DomainIdentity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
