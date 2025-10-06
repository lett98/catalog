package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import vn.ghtk.demo.catalog.domain.attribute.AttributeConfig;
import vn.ghtk.demo.catalog.domain.mp.VariantCodeType;

import java.util.List;

@Getter
@Setter
@Validated
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateProductRequest {
    @NotNull
    private Integer categoryId;
    @NotNull
    private Integer brandId;
    @NotNull
    private String model;
    @NotNull
    private String title;
}
