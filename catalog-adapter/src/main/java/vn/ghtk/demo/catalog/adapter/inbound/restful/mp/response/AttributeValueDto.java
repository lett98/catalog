package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttributeValueDto {
    private Integer attributeId;
    private String attributeName;
    private Object value;
}
