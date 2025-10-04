package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response;

import java.util.List;

public class MasterVariantDto {
    private Integer id;
    private String name;
    private Short status;
    private List<AttributeValueDto> attributeValues;
    private List<String> imageUrls;
}
