package vn.ghtk.demo.catalog.domain.common;

import java.util.Objects;

public class DomainAggregate<T extends DomainIdentity<?>> {
    protected T id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainAggregate<?> that = (DomainAggregate<?>) o;
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
