package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MasterProductListDto {
    private Integer id;
    private String title;
    private String category;
    private BigDecimal price;
    private String currency;
    private Short status;
}
