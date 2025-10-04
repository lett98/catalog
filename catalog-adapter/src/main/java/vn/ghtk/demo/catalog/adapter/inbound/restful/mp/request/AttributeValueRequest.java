package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import vn.ghtk.demo.catalog.domain.attribute.AttributeId;
import vn.ghtk.demo.catalog.domain.attribute.AttributeValue;

@Getter
@Setter
@Validated
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttributeValueRequest {
    @NotNull
    private Integer attributeId;

    @NotEmpty
    private String value;

    @JsonIgnore
    public AttributeValue attributeValue() {
        return new AttributeValue(new AttributeId(attributeId), value);
    }
}
