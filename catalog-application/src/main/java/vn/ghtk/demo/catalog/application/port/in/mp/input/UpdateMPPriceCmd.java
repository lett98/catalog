package vn.ghtk.demo.catalog.application.port.in.mp.input;

import vn.ghtk.demo.catalog.domain.Money;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;

public record UpdateMPPriceCmd(MasterProductId masterProductId,
                               Money newPrice) {
}
