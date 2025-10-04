package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MasterProductDto {
    private Integer id;
    private String title;
    private String category;
    private BigDecimal price;
    private String currency;
    private Short status;
    private String description;
    private List<AttributeConfigDto> attributeConfigs;
    private List<MasterVariantListDto> variants;
}
