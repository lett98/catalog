package vn.ghtk.demo.catalog.domain.mp;

import vn.ghtk.demo.catalog.common.exception.FoundedException;
import vn.ghtk.demo.catalog.domain.Money;
import vn.ghtk.demo.catalog.domain.attribute.AttributeConfig;
import vn.ghtk.demo.catalog.domain.attribute.AttributeId;
import vn.ghtk.demo.catalog.domain.attribute.AttributeValue;
import vn.ghtk.demo.catalog.domain.category.CategoryId;
import vn.ghtk.demo.catalog.domain.common.DomainAggregate;
import vn.ghtk.demo.catalog.domain.common.DomainEventCollector;
import vn.ghtk.demo.catalog.domain.common.id.IdGeneratorFactory;
import vn.ghtk.demo.catalog.domain.mp.event.*;
import vn.ghtk.demo.catalog.domain.mp.exception.VariantRequiredAttributeException;
import vn.ghtk.demo.catalog.domain.mp.exception.InvalidMasterProductState;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMVException;
import vn.ghtk.demo.catalog.domain.mp.exception.PublishMasterProductException;
import vn.ghtk.demo.catalog.domain.mp.spec.EditMasterProductSpec;
import vn.ghtk.demo.catalog.domain.mp.spec.EditMasterVariantSpec;
import vn.ghtk.demo.catalog.domain.mp.spec.EditProductVariantSpec;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class MasterProduct extends DomainAggregate<MasterProductId> {
    private CategoryId categoryId;
    private String description;
    private MasterProductTitle title;
    private Money basePrice;
    private List<AttributeConfig> attributeConfigs;
    private MasterProductStatus status;
    private List<MasterVariant> variants;

    public MasterProduct(CategoryId categoryId,
                         MasterProductTitle title) {
        this.id = new MasterProductId(IdGeneratorFactory.integerIdGenerator().generateId());
        this.categoryId = categoryId;
        this.title = title;
        this.status = MasterProductStatus.DRAFT;
        this.variants = new ArrayList<>();
        this.attributeConfigs = new ArrayList<>();
        DomainEventCollector.addEvent(new MasterProductCreatedEvent(this.id));
    }

    public void editProduct(EditMasterProductSpec spec) {
        if (spec.title() != null) {
            this.title = new MasterProductTitle(spec.title(), title.model(), title.brandId());
        }
        if (spec.description() != null) {
            this.description = spec.description();
        }
        if (spec.attributeConfigs() != null && !spec.attributeConfigs().isEmpty()) {
            this.attributeConfigs = spec.attributeConfigs();
        }
    }

    public void publishProduct() {
        if (!this.validatePublishProduct()) {
            throw new PublishMasterProductException(this.id());
        }
        this.status = MasterProductStatus.PUBLISHED;
        DomainEventCollector.addEvent(new MasterProductPublishedEvent(this.id));
    }

    private boolean validatePublishProduct() {
        return Objects.nonNull(this.categoryId) && Objects.nonNull(this.title);
    }

    public void retireProduct() {
        this.status = MasterProductStatus.RETIRED;
        DomainEventCollector.addEvent(new MasterProductRetiredEvent(this.id));
    }

    //TODO
    public void updateBasePrice(Money newPrice) {
        this.basePrice = newPrice;
        DomainEventCollector.addEvent(new MPPriceUpdatedEvent(this.id, newPrice));
    }

    //TODO
    public MasterVariant addVariant(List<AttributeValue> attributeValues,
                           String name,
                           String codeValue,
                           VariantCodeType codeType) {
        this.checkProductState();
        this.checkRequiredAttributes(attributeValues);
        MasterVariant variant = this.findVariantByAttributes(attributeValues);
        if (variant != null) {
            throw new FoundedException("Master variant already exists, id = [" + variant.id()+ "]");
        }
        MasterVariant newVariant = new MasterVariant(attributeValues, name, codeValue, codeType);
        this.variants.add(newVariant);
        DomainEventCollector.addEvent(new MasterVariantCreatedEvent(this.id, newVariant.id()));
        return newVariant;
    }

    public void editVariant(EditProductVariantSpec spec) {
        MasterVariant variant = this.findVariantById(spec.masterVariantId());
        EditMasterVariantSpec variantSpec = new EditMasterVariantSpec(spec.images(), spec.name());
        variant.editVariant(variantSpec);
    }

    public void publishVariant(MasterVariantId masterVariantId) {
        this.checkProductState();
        MasterVariant variant = this.findVariantById(masterVariantId);
        variant.publishVariant();
        DomainEventCollector.addEvent(new MasterVariantPublishedEvent(this.id, masterVariantId));
    }

    public void retireVariant(MasterVariantId masterVariantId) {
        MasterVariant variant = this.findVariantById(masterVariantId);
        variant.retireVariant();
        DomainEventCollector.addEvent(new MasterVariantRetiredEvent(this.id, masterVariantId));

    }

    private MasterVariant findVariantByAttributes(List<AttributeValue> searchAttributes) {
        return variants.stream()
                .filter(s -> s.hasAttributes(searchAttributes))
                .findFirst().orElse(null);
    }

    private MasterVariant findVariantById(MasterVariantId variantId) {
        Map<MasterVariantId, MasterVariant> variantById = variants.stream()
                .collect(toMap(MasterVariant::id, Function.identity()));
        MasterVariant masterVariant = variantById.getOrDefault(variantId, null);
        if (masterVariant == null) {
            throw new NotFoundMVException(this.id, variantId);
        }
        return masterVariant;
    }

    private void checkProductState() {
        if (!this.status.equals(MasterProductStatus.PUBLISHED)) {
            throw new InvalidMasterProductState(this.id, this.status);
        }
    }

    private void checkRequiredAttributes(List<AttributeValue> attributeValues) {
        Set<AttributeId> requiredAttributes = attributeConfigs.stream()
                .filter(AttributeConfig::mandatory)
                .map(AttributeConfig::attributeId)
                .collect(Collectors.toSet());
        Set<AttributeId> providedAttributes = attributeValues.stream()
                .map(AttributeValue::attributeId)
                .collect(Collectors.toSet());
        if (!providedAttributes.containsAll(requiredAttributes)) {
            throw new VariantRequiredAttributeException("MasterVariant does not include all required attributes.");
        }
    }

    public MasterProductStatus status() {
        return status;
    }

    public MasterProductTitle title() {
        return title;
    }

    public String description() {
        return description;
    }

    public List<MasterVariant> variants() {
        return variants;
    }

    public List<AttributeConfig> attributeConfigs() {
        return attributeConfigs;
    }

    public Money basePrice() {
        return basePrice;
    }
}
