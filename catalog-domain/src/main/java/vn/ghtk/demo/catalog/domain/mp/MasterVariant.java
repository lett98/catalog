package vn.ghtk.demo.catalog.domain.mp;

import vn.ghtk.demo.catalog.common.utils.CollectionUtil;
import vn.ghtk.demo.catalog.domain.ImageUrl;
import vn.ghtk.demo.catalog.domain.attribute.AttributeValue;
import vn.ghtk.demo.catalog.domain.common.DomainEntity;
import vn.ghtk.demo.catalog.domain.common.id.IdGeneratorFactory;
import vn.ghtk.demo.catalog.domain.mp.exception.PublishMasterVariantException;
import vn.ghtk.demo.catalog.domain.mp.spec.EditMasterVariantSpec;

import java.util.ArrayList;
import java.util.List;

public class MasterVariant extends DomainEntity<MasterVariantId> {
    private List<AttributeValue> attributeValues;
    private MasterVariantCode code;
    private MasterVariantStatus status;
    private MasterVariantName name;
    private List<ImageUrl> images;

    MasterVariant(List<AttributeValue> attributeValues,
                  String name,
                  String codeValue,
                  VariantCodeType codeType) {
        super(new MasterVariantId(IdGeneratorFactory.integerNumberGenerator().generateId()));
        this.attributeValues = attributeValues;
        this.name = new MasterVariantName(name);
        this.code = new MasterVariantCode(codeValue,codeType);
        this.status = MasterVariantStatus.DRAFT;
        this.images = new ArrayList<>();
    }

    void editVariant(EditMasterVariantSpec spec) {
        this.name = spec.name();
        this.images = new ArrayList<>(spec.images());
    }

    void publishVariant() {
        if (images.isEmpty()) {
            throw new PublishMasterVariantException(this.id);
        }
        this.status = MasterVariantStatus.PUBLISHED;
    }

    void retireVariant() {
        this.status = MasterVariantStatus.RETIRED;
    }

    boolean hasAttributes(List<AttributeValue> searchAttributes) {
        return CollectionUtil.equals(attributeValues, searchAttributes);
    }
}
