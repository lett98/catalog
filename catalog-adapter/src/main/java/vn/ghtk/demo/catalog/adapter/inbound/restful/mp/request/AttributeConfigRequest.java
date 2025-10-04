package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import vn.ghtk.demo.catalog.domain.attribute.AttributeConfig;
import vn.ghtk.demo.catalog.domain.attribute.AttributeId;

@Getter
@Setter
@Validated
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttributeConfigRequest {
    @NotNull
    private Integer attributeId;

    @NotNull
    private Boolean mandatory;

    @JsonIgnore
    public AttributeConfig attributeConfig() {
        return new AttributeConfig(new AttributeId(attributeId), mandatory);
    }
}
