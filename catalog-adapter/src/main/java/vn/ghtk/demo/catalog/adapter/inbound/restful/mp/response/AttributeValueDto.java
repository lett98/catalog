package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response;

import lombok.Getter;

@Getter
public class AttributeValueDto {
    private Integer attributeId;
    private String attributeName;
    private Object value;
}
