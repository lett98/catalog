package vn.ghtk.demo.catalog.domain.common.id;

public interface IdGenerator<T> {
    T generateId();
}
