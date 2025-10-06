package vn.ghtk.demo.catalog.domain.mp;

import org.junit.jupiter.api.Test;
import vn.ghtk.demo.catalog.domain.ImageUrl;
import vn.ghtk.demo.catalog.domain.attribute.AttributeId;
import vn.ghtk.demo.catalog.domain.attribute.AttributeValue;
import vn.ghtk.demo.catalog.domain.mp.exception.PublishMasterVariantException;
import vn.ghtk.demo.catalog.domain.mp.spec.EditMasterVariantSpec;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MasterVariantTest {

    @Test
    void createMasterVariant_shouldInitializeWithDraftStatus() {
        // Given
        List<AttributeValue> attributes = List.of(
                new AttributeValue(new AttributeId(1), "Red")
        );

        // When
        MasterVariant variant = new MasterVariant(
                attributes,
                "Red Variant",
                "SKU-001",
                VariantCodeType.GTIN
        );

        // Then
        assertNotNull(variant.id());
        assertEquals(MasterVariantStatus.DRAFT, variant.status());
        assertEquals("Red Variant", variant.name().name());
        assertEquals("SKU-001", variant.code().code());
        assertEquals(VariantCodeType.GTIN, variant.code().type());
        assertTrue(variant.images().isEmpty());
    }

    @Test
    void createMasterVariant_shouldStoreAttributeValues() {
        // Given
        List<AttributeValue> attributes = List.of(
                new AttributeValue(new AttributeId(1), "Red"),
                new AttributeValue(new AttributeId(2), "Large")
        );

        // When
        MasterVariant variant = new MasterVariant(
                attributes,
                "Red Large",
                "SKU-002",
                VariantCodeType.GTIN
        );

        // Then
        assertEquals(2, variant.attributeValues().size());
    }

    @Test
    void editVariant_shouldUpdateNameAndImages() {
        // Given
        MasterVariant variant = new MasterVariant(
                Collections.emptyList(),
                "Original Name",
                "SKU-001",
                VariantCodeType.GTIN
        );

        List<ImageUrl> images = List.of(
                new ImageUrl("http://example.com/image1.jpg"),
                new ImageUrl("http://example.com/image2.jpg")
        );
        EditMasterVariantSpec spec = new EditMasterVariantSpec(
                images,
                new MasterVariantName("Updated Name")
        );

        // When
        variant.editVariant(spec);

        // Then
        assertEquals("Updated Name", variant.name().name());
        assertEquals(2, variant.images().size());
        assertEquals("http://example.com/image1.jpg", variant.images().get(0).url());
    }

    @Test
    void editVariant_shouldUpdateOnlyImagesWhenNameIsNull() {
        // Given
        MasterVariant variant = new MasterVariant(
                Collections.emptyList(),
                "Original Name",
                "SKU-001",
                VariantCodeType.GTIN
        );

        List<ImageUrl> images = List.of(new ImageUrl("http://example.com/image.jpg"));
        EditMasterVariantSpec spec = new EditMasterVariantSpec(images, null);

        // When
        variant.editVariant(spec);

        // Then
        assertNull(variant.name());
        assertEquals(1, variant.images().size());
    }

    @Test
    void publishVariant_shouldChangeStatusToPublished() {
        // Given
        MasterVariant variant = new MasterVariant(
                Collections.emptyList(),
                "Variant",
                "SKU-001",
                VariantCodeType.GTIN
        );

        List<ImageUrl> images = List.of(new ImageUrl("http://example.com/image.jpg"));
        EditMasterVariantSpec spec = new EditMasterVariantSpec(images, null);
        variant.editVariant(spec);

        // When
        variant.publishVariant();

        // Then
        assertEquals(MasterVariantStatus.PUBLISHED, variant.status());
    }

    @Test
    void publishVariant_shouldThrowExceptionWhenNoImages() {
        // Given
        MasterVariant variant = new MasterVariant(
                Collections.emptyList(),
                "Variant",
                "SKU-001",
                VariantCodeType.GTIN
        );

        // When & Then
        assertThrows(PublishMasterVariantException.class, variant::publishVariant);
    }

    @Test
    void retireVariant_shouldChangeStatusToRetired() {
        // Given
        MasterVariant variant = new MasterVariant(
                Collections.emptyList(),
                "Variant",
                "SKU-001",
                VariantCodeType.GTIN
        );

        // When
        variant.retireVariant();

        // Then
        assertEquals(MasterVariantStatus.RETIRED, variant.status());
    }

    @Test
    void hasAttributes_shouldReturnTrueForMatchingAttributes() {
        // Given
        List<AttributeValue> attributes = List.of(
                new AttributeValue(new AttributeId(1), "Red"),
                new AttributeValue(new AttributeId(2), "Large")
        );

        MasterVariant variant = new MasterVariant(
                attributes,
                "Red Large",
                "SKU-001",
                VariantCodeType.GTIN
        );

        List<AttributeValue> searchAttributes = List.of(
                new AttributeValue(new AttributeId(1), "Red"),
                new AttributeValue(new AttributeId(2), "Large")
        );

        // When
        boolean result = variant.hasAttributes(searchAttributes);

        // Then
        assertTrue(result);
    }

    @Test
    void hasAttributes_shouldReturnFalseForNonMatchingAttributes() {
        // Given
        List<AttributeValue> attributes = List.of(
                new AttributeValue(new AttributeId(1), "Red")
        );

        MasterVariant variant = new MasterVariant(
                attributes,
                "Red",
                "SKU-001",
                VariantCodeType.GTIN
        );

        List<AttributeValue> searchAttributes = List.of(
                new AttributeValue(new AttributeId(1), "Blue")
        );

        // When
        boolean result = variant.hasAttributes(searchAttributes);

        // Then
        assertFalse(result);
    }

    @Test
    void hasAttributes_shouldReturnFalseForDifferentAttributeCount() {
        // Given
        List<AttributeValue> attributes = List.of(
                new AttributeValue(new AttributeId(1), "Red")
        );

        MasterVariant variant = new MasterVariant(
                attributes,
                "Red",
                "SKU-001",
                VariantCodeType.GTIN
        );

        List<AttributeValue> searchAttributes = List.of(
                new AttributeValue(new AttributeId(1), "Red"),
                new AttributeValue(new AttributeId(2), "Large")
        );

        // When
        boolean result = variant.hasAttributes(searchAttributes);

        // Then
        assertFalse(result);
    }

    @Test
    void createVariant_withDifferentCodeTypes() {
        // Given & When
        MasterVariant skuVariant = new MasterVariant(
                Collections.emptyList(),
                "SKU Variant",
                "SKU-001",
                VariantCodeType.GTIN
        );

        MasterVariant upcVariant = new MasterVariant(
                Collections.emptyList(),
                "UPC Variant",
                "123456789012",
                VariantCodeType.UPC
        );

        // Then
        assertEquals(VariantCodeType.GTIN, skuVariant.code().type());
        assertEquals(VariantCodeType.UPC, upcVariant.code().type());
    }
}
