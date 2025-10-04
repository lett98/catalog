package vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Validated
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EditMasterProductRequest {
    @NotNull
    private Integer masterProductId;
    private String description;
    private String title;
    private List<AttributeConfigRequest> attributeConfigs;
}
