package vn.ghtk.demo.catalog.application.port.in.mp.input;

import java.util.List;

public record MasterProductListQuery(int page,
                                     int size,
                                     List<String> sortFields,
                                     Integer categoryId,
                                     Short status) {
}
