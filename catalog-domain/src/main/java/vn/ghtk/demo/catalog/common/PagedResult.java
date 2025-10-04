package vn.ghtk.demo.catalog.common;

import java.util.List;

public record PagedResult<T>(List<T> detail, long total) {
}
