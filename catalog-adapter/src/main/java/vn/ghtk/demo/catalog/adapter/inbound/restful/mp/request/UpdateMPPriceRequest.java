package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import vn.ghtk.demo.catalog.domain.Currency;

import java.math.BigDecimal;

@Getter
@Setter
@Validated
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateMPPriceRequest {
    @NotNull
    private Integer masterProductId;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Currency currency;
}
