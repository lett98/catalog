package vn.ghtk.demo.catalog.domain;

import java.math.BigDecimal;

public record Money(BigDecimal amount, Currency currency) {
}
