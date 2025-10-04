package vn.ghtk.demo.catalog.domain.mp;

import vn.ghtk.demo.catalog.common.util.CollectionUtil;
import vn.ghtk.demo.catalog.domain.ImageUrl;
import vn.ghtk.demo.catalog.domain.attribute.AttributeValue;
import vn.ghtk.demo.catalog.domain.common.DomainEntity;
import vn.ghtk.demo.catalog.domain.common.id.IdGeneratorFactory;

import java.util.ArrayList;
import java.util.List;

public class MasterVariant extends DomainEntity<MasterVariantId> {
    private List<AttributeValue> attributeValues;
    private MasterVariantCode code;
    private MasterVariantStatus status;
    private MasterVariantName name;
    private List<ImageUrl> images;

    private MasterVariant(MasterVariantId id) {
        super(id);
    }

    MasterVariant(List<AttributeValue> attributeValues,
                  String name,
                  String codeValue,
                  VariantCodeType codeType,
                  List<String> imageUrls) {
        super(new MasterVariantId(IdGeneratorFactory.integerNumberGenerator().generateId()));
        this.attributeValues = attributeValues;
        this.name = new MasterVariantName(name);
        this.code = new MasterVariantCode(codeValue,codeType);
        this.status = MasterVariantStatus.DRAFT;
        this.images = imageUrls.isEmpty() ? new ArrayList<>() : imageUrls.stream().map(ImageUrl::new).toList();
    }

    void publishVariant() {
        this.status = MasterVariantStatus.PUBLISHED;
    }

    void retireVariant() {
        this.status = MasterVariantStatus.RETIRED;
    }

    boolean hasAttributes(List<AttributeValue> searchAttributes) {
        return CollectionUtil.compare(attributeValues, searchAttributes);
    }
}
