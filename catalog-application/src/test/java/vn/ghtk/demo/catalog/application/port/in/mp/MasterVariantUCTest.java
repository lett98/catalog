package vn.ghtk.demo.catalog.application.port.in.mp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.ghtk.demo.catalog.application.port.in.mp.input.AddMasterVariantCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterVariantCmd;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductRepository;
import vn.ghtk.demo.catalog.domain.BrandId;
import vn.ghtk.demo.catalog.domain.ImageUrl;
import vn.ghtk.demo.catalog.domain.category.CategoryId;
import vn.ghtk.demo.catalog.domain.common.DomainEventCollector;
import vn.ghtk.demo.catalog.domain.mp.*;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMPException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MasterVariantUCTest {

    @Mock
    private MasterProductRepository masterProductRepository;

    private MasterVariantUC masterVariantUC;

    @BeforeEach
    void setUp() {
        DomainEventCollector.clear();
        masterVariantUC = new MasterVariantUC(masterProductRepository);
    }

    @Test
    void createVariant_shouldCreateNewVariant() {
        // Given
        MasterProductId productId = new MasterProductId(1);
        AddMasterVariantCmd cmd = new AddMasterVariantCmd(
                productId,
                Collections.emptyList(),
                "Variant Name",
                "SKU-001",
                VariantCodeType.GTIN
        );

        MasterProduct product = new MasterProduct(
                new CategoryId(1),
                new MasterProductTitle("Product", "Model-1", new BrandId(100))
        );
        product.publishProduct();

        when(masterProductRepository.findProductById(productId)).thenReturn(product);

        // When
        MasterVariantId variantId = masterVariantUC.createVariant(cmd);

        // Then
        assertNotNull(variantId);
        assertEquals(1, product.variants().size());
        verify(masterProductRepository).findProductById(productId);
        verify(masterProductRepository).save(product);
    }

    @Test
    void createVariant_shouldThrowExceptionWhenProductNotFound() {
        // Given
        MasterProductId productId = new MasterProductId(999);
        AddMasterVariantCmd cmd = new AddMasterVariantCmd(
                productId,
                Collections.emptyList(),
                "Variant Name",
                "SKU-001",
                VariantCodeType.GTIN
        );

        when(masterProductRepository.findProductById(productId)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundMPException.class, () -> masterVariantUC.createVariant(cmd));
        verify(masterProductRepository, never()).save(any(MasterProduct.class));
    }

    @Test
    void editVariant_shouldEditExistingVariant() {
        // Given
        MasterProductId productId = new MasterProductId(1);

        MasterProduct product = new MasterProduct(
                new CategoryId(1),
                new MasterProductTitle("Product", "Model-1", new BrandId(100))
        );
        product.publishProduct();
        MasterVariant variant = product.addVariant(
                Collections.emptyList(),
                "Original Name",
                "SKU-001",
                VariantCodeType.GTIN
        );

        EditMasterVariantCmd cmd = new EditMasterVariantCmd(
                productId,
                variant.id(),
                List.of(new ImageUrl("http://example.com/image.jpg")),
                new MasterVariantName("Updated Name")
        );

        when(masterProductRepository.findProductById(productId)).thenReturn(product);

        // When
        masterVariantUC.editVariant(cmd);

        // Then
        assertEquals("Updated Name", variant.name().name());
        verify(masterProductRepository).findProductById(productId);
        verify(masterProductRepository).save(product);
    }

    @Test
    void editVariant_shouldThrowExceptionWhenProductNotFound() {
        // Given
        MasterProductId productId = new MasterProductId(999);
        MasterVariantId variantId = new MasterVariantId(1);
        EditMasterVariantCmd cmd = new EditMasterVariantCmd(
                productId,
                variantId,
                Collections.emptyList(),
                new MasterVariantName("Name")
        );

        when(masterProductRepository.findProductById(productId)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundMPException.class, () -> masterVariantUC.editVariant(cmd));
        verify(masterProductRepository, never()).save(any(MasterProduct.class));
    }

    @Test
    void publishVariant_shouldPublishVariant() {
        // Given
        MasterProductId productId = new MasterProductId(1);

        MasterProduct product = new MasterProduct(
                new CategoryId(1),
                new MasterProductTitle("Product", "Model-1", new BrandId(100))
        );
        product.publishProduct();
        MasterVariant variant = product.addVariant(
                Collections.emptyList(),
                "Variant",
                "SKU-001",
                VariantCodeType.GTIN
        );

        when(masterProductRepository.findProductById(productId)).thenReturn(product);

        // Add images to enable publishing
        EditMasterVariantCmd editCmd = new EditMasterVariantCmd(
                productId,
                variant.id(),
                List.of(new ImageUrl("http://example.com/image.jpg")),
                null
        );
        masterVariantUC.editVariant(editCmd);

        // When
        masterVariantUC.publishVariant(productId, variant.id());

        // Then
        assertEquals(MasterVariantStatus.PUBLISHED, variant.status());
        verify(masterProductRepository, times(2)).findProductById(productId);
        verify(masterProductRepository, times(2)).save(product);
    }

    @Test
    void publishVariant_shouldThrowExceptionWhenProductNotFound() {
        // Given
        MasterProductId productId = new MasterProductId(999);
        MasterVariantId variantId = new MasterVariantId(1);

        when(masterProductRepository.findProductById(productId)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundMPException.class, () ->
                masterVariantUC.publishVariant(productId, variantId)
        );
        verify(masterProductRepository, never()).save(any(MasterProduct.class));
    }

    @Test
    void retireVariant_shouldRetireVariant() {
        // Given
        MasterProductId productId = new MasterProductId(1);

        MasterProduct product = new MasterProduct(
                new CategoryId(1),
                new MasterProductTitle("Product", "Model-1", new BrandId(100))
        );
        product.publishProduct();
        MasterVariant variant = product.addVariant(
                Collections.emptyList(),
                "Variant",
                "SKU-001",
                VariantCodeType.GTIN
        );

        when(masterProductRepository.findProductById(productId)).thenReturn(product);

        // When
        masterVariantUC.retireVariant(productId, variant.id());

        // Then
        assertEquals(MasterVariantStatus.RETIRED, variant.status());
        verify(masterProductRepository).findProductById(productId);
        verify(masterProductRepository).save(product);
    }

    @Test
    void retireVariant_shouldThrowExceptionWhenProductNotFound() {
        // Given
        MasterProductId productId = new MasterProductId(999);
        MasterVariantId variantId = new MasterVariantId(1);

        when(masterProductRepository.findProductById(productId)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundMPException.class, () ->
                masterVariantUC.retireVariant(productId, variantId)
        );
        verify(masterProductRepository, never()).save(any(MasterProduct.class));
    }
}
