package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MasterProductListDto {
    private Integer id;
    private String title;
    private String category;
    private BigDecimal price;
    private String currency;
    private Short status;
}
