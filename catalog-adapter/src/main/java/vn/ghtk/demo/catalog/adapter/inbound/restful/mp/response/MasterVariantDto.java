package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MasterVariantDto {
    private Integer id;
    private String name;
    private Short status;
    private List<AttributeValueDto> attributeValues;
    private List<String> imageUrls;
}
