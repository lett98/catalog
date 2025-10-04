package vn.ghtk.demo.catalog.domain.mp;

import vn.ghtk.demo.catalog.common.exception.FoundedException;
import vn.ghtk.demo.catalog.domain.BrandId;
import vn.ghtk.demo.catalog.domain.Money;
import vn.ghtk.demo.catalog.domain.attribute.AttributeConfig;
import vn.ghtk.demo.catalog.domain.attribute.AttributeValue;
import vn.ghtk.demo.catalog.domain.category.CategoryId;
import vn.ghtk.demo.catalog.domain.common.DomainAggregate;
import vn.ghtk.demo.catalog.domain.common.DomainEventCollector;
import vn.ghtk.demo.catalog.domain.common.id.IdGeneratorFactory;
import vn.ghtk.demo.catalog.domain.mp.event.*;
import vn.ghtk.demo.catalog.domain.mp.exception.InvalidMasterProductState;
import vn.ghtk.demo.catalog.domain.mp.exception.PublishMasterProductException;

import java.util.*;
import java.util.function.Function;

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
                         BrandId brandId,
                         String model,
                         String title,
                         String description,
                         List<AttributeConfig> attributeConfigs) {
        this.id = new MasterProductId(IdGeneratorFactory.integerNumberGenerator().generateId());
        this.categoryId = categoryId;
        this.title = new MasterProductTitle(title, model, brandId);
        this.description = description;
        this.attributeConfigs = attributeConfigs;
        this.status = MasterProductStatus.DRAFT;
        this.variants = new ArrayList<>();
        DomainEventCollector.addEvent(new MasterProductCreatedEvent(this.id));
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
    public void editDescription(String description) {
        this.description = description;
    }

    //TODO
    public void updateBasePrice(Money newPrice) {
        this.basePrice = newPrice;
    }

    //TODO
    public void addVariant(List<AttributeValue> attributeValues,
                           String name,
                           String codeValue,
                           VariantCodeType codeType,
                           List<String> imageUrls) {
        if (!this.status.equals(MasterProductStatus.PUBLISHED)) {
            throw new InvalidMasterProductState(this.id, this.status);
        }
        
        MasterVariant variant = this.findVariantByAttributes(attributeValues);
        if (variant != null) {
            throw new FoundedException("Master variant already exists");
        }

        MasterVariant newVariant = new MasterVariant(attributeValues, name,codeValue,codeType, imageUrls);
        this.variants.add(newVariant);
        DomainEventCollector.addEvent(new MasterVariantCreatedEvent(this.id, newVariant.id()));

    }

    public void publishVariant(MasterVariantId masterVariantId) {
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
            throw new NoSuchElementException("Master variant not found.");
        }
        return masterVariant;
    }
}
