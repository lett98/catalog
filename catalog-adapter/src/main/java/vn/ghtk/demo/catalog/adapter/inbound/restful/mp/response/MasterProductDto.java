package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response;

import java.math.BigDecimal;
import java.util.List;

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
