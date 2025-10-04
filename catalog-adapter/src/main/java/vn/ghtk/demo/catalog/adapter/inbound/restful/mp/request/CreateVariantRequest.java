package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import vn.ghtk.demo.catalog.domain.mp.VariantCodeType;

import java.util.List;

@Getter
@Setter
@Validated
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateVariantRequest {
    @NotNull
    private Integer masterProductId;
    @NotNull@NotEmpty@NotBlank
    private List<AttributeValueRequest> attributeValues;
    @NotNull
    private String name;
    @NotNull
    private String codeValue;
    @NotNull
    private VariantCodeType codeType;
}
