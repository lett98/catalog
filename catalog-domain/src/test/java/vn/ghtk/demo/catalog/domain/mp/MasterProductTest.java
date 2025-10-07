package vn.ghtk.demo.catalog.domain.mp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.ghtk.demo.catalog.common.exception.FoundedException;
import vn.ghtk.demo.catalog.domain.Money;
import vn.ghtk.demo.catalog.domain.attribute.AttributeConfig;
import vn.ghtk.demo.catalog.domain.attribute.AttributeId;
import vn.ghtk.demo.catalog.domain.attribute.AttributeValue;
import vn.ghtk.demo.catalog.domain.category.CategoryId;
import vn.ghtk.demo.catalog.domain.common.DomainEventCollector;
import vn.ghtk.demo.catalog.domain.mp.event.*;
import vn.ghtk.demo.catalog.domain.mp.exception.InvalidMasterProductState;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMVException;
import vn.ghtk.demo.catalog.domain.mp.exception.PublishMasterProductException;
import vn.ghtk.demo.catalog.domain.mp.exception.VariantRequiredAttributeException;
import vn.ghtk.demo.catalog.domain.mp.spec.EditMasterProductSpec;
import vn.ghtk.demo.catalog.domain.mp.spec.EditProductVariantSpec;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MasterProductTest {

    private CategoryId categoryId;
    private MasterProductTitle title;

    @BeforeEach
    void setUp() {
        DomainEventCollector.clear();
        categoryId = new CategoryId(1);
        title = new MasterProductTitle("Product Title", "Model-1", new vn.ghtk.demo.catalog.domain.BrandId(100));
    }

    @AfterEach
    void tearDown() {
        DomainEventCollector.clear();
    }

    @Test
    void createMasterProduct_shouldInitializeWithDraftStatus() {
        // When
        MasterProduct product = new MasterProduct(categoryId, title);

        // Then
        assertNotNull(product.id());
        assertEquals(MasterProductStatus.DRAFT, product.status());
        assertEquals(1, DomainEventCollector.getEvents().size());
        assertTrue(DomainEventCollector.getEvents().get(0) instanceof MasterProductCreatedEvent);
    }

    @Test
    void editProduct_shouldUpdateTitleWhenProvided() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        DomainEventCollector.clear();
        EditMasterProductSpec spec = new EditMasterProductSpec(
                "Description",
                "New Title",
                Collections.emptyList()
        );

        // When
        product.editProduct(spec);

        // Then
        assertEquals("New Title", product.title().title());
    }

    @Test
    void editProduct_shouldUpdateDescriptionWhenProvided() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        EditMasterProductSpec spec = new EditMasterProductSpec(
                "New Description",
                null,
                Collections.emptyList()
        );

        // When
        product.editProduct(spec);

        // Then
        assertEquals("New Description", product.description());
    }

    @Test
    void editProduct_shouldUpdateAttributeConfigsWhenProvided() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        List<AttributeConfig> configs = List.of(
                new AttributeConfig(new AttributeId(1), true)
        );
        EditMasterProductSpec spec = new EditMasterProductSpec(
                null,
                null,
                configs
        );

        // When
        product.editProduct(spec);

        // Then
        assertEquals(1, product.attributeConfigs().size());
    }

    @Test
    void publishProduct_shouldChangeStatusToPublished() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        DomainEventCollector.clear();

        // When
        product.publishProduct();

        // Then
        assertEquals(MasterProductStatus.PUBLISHED, product.status());
        assertEquals(1, DomainEventCollector.getEvents().size());
        assertTrue(DomainEventCollector.getEvents().get(0) instanceof MasterProductPublishedEvent);
    }

    @Test
    void publishProduct_shouldThrowExceptionWhenCategoryIdIsNull() {
        // Given
        MasterProduct product = new MasterProduct(null, title);

        // When & Then
        assertThrows(PublishMasterProductException.class, product::publishProduct);
    }

    @Test
    void publishProduct_shouldThrowExceptionWhenTitleIsNull() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, null);

        // When & Then
        assertThrows(PublishMasterProductException.class, product::publishProduct);
    }

    @Test
    void retireProduct_shouldChangeStatusToRetired() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        DomainEventCollector.clear();

        // When
        product.retireProduct();

        // Then
        assertEquals(MasterProductStatus.RETIRED, product.status());
        assertEquals(1, DomainEventCollector.getEvents().size());
        assertTrue(DomainEventCollector.getEvents().get(0) instanceof MasterProductRetiredEvent);
    }

    @Test
    void updateBasePrice_shouldUpdatePriceAndEmitEvent() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        DomainEventCollector.clear();
        Money newPrice = new Money(new BigDecimal("99.99"), vn.ghtk.demo.catalog.domain.Currency.USD);

        // When
        product.updateBasePrice(newPrice);

        // Then
        assertEquals(newPrice, product.basePrice());
        assertEquals(1, DomainEventCollector.getEvents().size());
        assertTrue(DomainEventCollector.getEvents().get(0) instanceof MPPriceUpdatedEvent);
    }

    @Test
    void addVariant_shouldThrowExceptionWhenProductNotPublished() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        List<AttributeValue> attributes = Collections.emptyList();

        // When & Then
        assertThrows(InvalidMasterProductState.class, () ->
                product.addVariant(attributes, "Variant Name", "CODE-1", VariantCodeType.GTIN)
        );
    }

    @Test
    void addVariant_shouldThrowExceptionWhenRequiredAttributesAreMissing() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        List<AttributeConfig> configs = List.of(
                new AttributeConfig(new AttributeId(1), true),
                new AttributeConfig(new AttributeId(2), true)
        );
        EditMasterProductSpec spec = new EditMasterProductSpec(null, null, configs);
        product.editProduct(spec);
        product.publishProduct();

        List<AttributeValue> attributes = List.of(
                new AttributeValue(new AttributeId(1), "Red")
        );

        // When & Then
        assertThrows(VariantRequiredAttributeException.class, () ->
                product.addVariant(attributes, "Variant Name", "CODE-1", VariantCodeType.GTIN)
        );
    }

    @Test
    void addVariant_shouldCreateVariantWhenAllRequiredAttributesProvided() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        List<AttributeConfig> configs = List.of(
                new AttributeConfig(new AttributeId(1), true),
                new AttributeConfig(new AttributeId(2), true)
        );
        EditMasterProductSpec spec = new EditMasterProductSpec(null, null, configs);
        product.editProduct(spec);
        product.publishProduct();
        DomainEventCollector.clear();

        List<AttributeValue> attributes = List.of(
                new AttributeValue(new AttributeId(1), "Red"),
                new AttributeValue(new AttributeId(2), "Large")
        );

        // When
        MasterVariant variant = product.addVariant(attributes, "Red Large", "CODE-1", VariantCodeType.GTIN);

        // Then
        assertNotNull(variant);
        assertEquals(1, product.variants().size());
        assertEquals(1, DomainEventCollector.getEvents().size());
        assertTrue(DomainEventCollector.getEvents().get(0) instanceof MasterVariantCreatedEvent);
    }

    @Test
    void addVariant_shouldThrowExceptionWhenDuplicateVariantExists() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        List<AttributeConfig> configs = List.of(
                new AttributeConfig(new AttributeId(1), true)
        );
        EditMasterProductSpec spec = new EditMasterProductSpec(null, null, configs);
        product.editProduct(spec);
        product.publishProduct();

        List<AttributeValue> attributes = List.of(
                new AttributeValue(new AttributeId(1), "Red")
        );

        product.addVariant(attributes, "Red Variant", "CODE-1", VariantCodeType.GTIN);

        // When & Then
        assertThrows(FoundedException.class, () ->
                product.addVariant(attributes, "Red Variant 2", "CODE-2", VariantCodeType.GTIN)
        );
    }

    @Test
    void editVariant_shouldUpdateVariantName() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        product.publishProduct();
        List<AttributeValue> attributes = new ArrayList<>();
        MasterVariant variant = product.addVariant(attributes, "Original Name", "CODE-1", VariantCodeType.GTIN);

        EditProductVariantSpec editSpec = new EditProductVariantSpec(
                variant.id(),
                Collections.emptyList(),
                new MasterVariantName("Updated Name")
        );

        // When
        product.editVariant(editSpec);

        // Then
        assertEquals("Updated Name", variant.name().name());
    }

    @Test
    void editVariant_shouldThrowExceptionWhenVariantNotFound() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        MasterVariantId nonExistentId = new MasterVariantId(999);
        EditProductVariantSpec editSpec = new EditProductVariantSpec(
                nonExistentId,
                Collections.emptyList(),
                new MasterVariantName("Name")
        );

        // When & Then
        assertThrows(NotFoundMVException.class, () -> product.editVariant(editSpec));
    }

    @Test
    void publishVariant_shouldThrowExceptionWhenProductNotPublished() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        MasterVariantId variantId = new MasterVariantId(1);

        // When & Then
        assertThrows(InvalidMasterProductState.class, () ->
                product.publishVariant(variantId)
        );
    }

    @Test
    void publishVariant_shouldPublishVariantWhenProductIsPublished() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        product.publishProduct();
        List<AttributeValue> attributes = List.of(
                new AttributeValue(new AttributeId(1), "Red")
        );
        MasterVariant variant = product.addVariant(attributes, "Red Variant", "CODE-1", VariantCodeType.GTIN);
        variant.editVariant(new vn.ghtk.demo.catalog.domain.mp.spec.EditMasterVariantSpec(
                List.of(new vn.ghtk.demo.catalog.domain.ImageUrl("http://image.com/1.jpg")),
                null
        ));
        DomainEventCollector.clear();

        // When
        product.publishVariant(variant.id());

        // Then
        assertEquals(MasterVariantStatus.PUBLISHED, variant.status());
        assertEquals(1, DomainEventCollector.getEvents().size());
        assertTrue(DomainEventCollector.getEvents().get(0) instanceof MasterVariantPublishedEvent);
    }

    @Test
    void retireVariant_shouldRetireVariant() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        product.publishProduct();
        MasterVariant variant = product.addVariant(Collections.emptyList(), "Variant", "CODE-1", VariantCodeType.GTIN);
        DomainEventCollector.clear();

        // When
        product.retireVariant(variant.id());

        // Then
        assertEquals(MasterVariantStatus.RETIRED, variant.status());
        assertEquals(1, DomainEventCollector.getEvents().size());
        assertTrue(DomainEventCollector.getEvents().get(0) instanceof MasterVariantRetiredEvent);
    }

    @Test
    void retireVariant_shouldThrowExceptionWhenVariantNotFound() {
        // Given
        MasterProduct product = new MasterProduct(categoryId, title);
        MasterVariantId nonExistentId = new MasterVariantId(999);

        // When & Then
        assertThrows(NotFoundMVException.class, () ->
                product.retireVariant(nonExistentId)
        );
    }
}
