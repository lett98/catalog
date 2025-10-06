package vn.ghtk.demo.catalog.application.port.in.mp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.ghtk.demo.catalog.application.port.in.mp.input.CreateProductCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterProductCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.UpdateMPPriceCmd;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductRepository;
import vn.ghtk.demo.catalog.application.port.out.mp.SearchMasterProductCriteria;
import vn.ghtk.demo.catalog.common.exception.FoundedException;
import vn.ghtk.demo.catalog.domain.Money;
import vn.ghtk.demo.catalog.domain.category.CategoryId;
import vn.ghtk.demo.catalog.domain.common.DomainEventCollector;
import vn.ghtk.demo.catalog.domain.mp.MasterProduct;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductTitle;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMPException;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MasterProductUCTest {

    @Mock
    private MasterProductRepository masterProductRepository;

    private MasterProductUC masterProductUC;

    @BeforeEach
    void setUp() {
        DomainEventCollector.clear();
        masterProductUC = new MasterProductUC(masterProductRepository);
    }

    @Test
    void createProduct_shouldCreateNewProduct() {
        // Given
        CreateProductCmd cmd = new CreateProductCmd(
                new CategoryId(1),
                new vn.ghtk.demo.catalog.domain.BrandId(100),
                "Model-1",
                "Test Product"
        );

        when(masterProductRepository.searchProduct(any(SearchMasterProductCriteria.class)))
                .thenReturn(null);

        // When
        MasterProductId productId = masterProductUC.createProduct(cmd);

        // Then
        assertNotNull(productId);
        verify(masterProductRepository).searchProduct(any(SearchMasterProductCriteria.class));
        verify(masterProductRepository).save(any(MasterProduct.class));
    }

    @Test
    void createProduct_shouldThrowExceptionWhenProductAlreadyExists() {
        // Given
        CreateProductCmd cmd = new CreateProductCmd(
                new CategoryId(1),
                new vn.ghtk.demo.catalog.domain.BrandId(100),
                "Model-1",
                "Test Product"
        );

        MasterProduct existingProduct = new MasterProduct(
                new CategoryId(1),
                new MasterProductTitle("Existing", "Model-1", new vn.ghtk.demo.catalog.domain.BrandId(100))
        );

        when(masterProductRepository.searchProduct(any(SearchMasterProductCriteria.class)))
                .thenReturn(existingProduct);

        // When & Then
        assertThrows(FoundedException.class, () -> masterProductUC.createProduct(cmd));
        verify(masterProductRepository, never()).save(any(MasterProduct.class));
    }

    @Test
    void editProduct_shouldEditExistingProduct() {
        // Given
        MasterProductId productId = new MasterProductId(1);
        EditMasterProductCmd cmd = new EditMasterProductCmd(
                productId,
                "Updated Description",
                "Updated Title",
                Collections.emptyList()
        );

        MasterProduct product = new MasterProduct(
                new CategoryId(1),
                new MasterProductTitle("Original", "Model-1", new vn.ghtk.demo.catalog.domain.BrandId(100))
        );

        when(masterProductRepository.findProductById(productId)).thenReturn(product);

        // When
        masterProductUC.editProduct(cmd);

        // Then
        assertEquals("Updated Title", product.title().title());
        verify(masterProductRepository).findProductById(productId);
        verify(masterProductRepository).save(product);
    }

    @Test
    void editProduct_shouldThrowExceptionWhenProductNotFound() {
        // Given
        MasterProductId productId = new MasterProductId(999);
        EditMasterProductCmd cmd = new EditMasterProductCmd(
                productId,
                "Description",
                "Title",
                Collections.emptyList()
        );

        when(masterProductRepository.findProductById(productId)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundMPException.class, () -> masterProductUC.editProduct(cmd));
        verify(masterProductRepository, never()).save(any(MasterProduct.class));
    }

    @Test
    void updateBasePrice_shouldUpdateProductPrice() {
        // Given
        MasterProductId productId = new MasterProductId(1);
        Money newPrice = new Money(new BigDecimal("99.99"), vn.ghtk.demo.catalog.domain.Currency.USD);
        UpdateMPPriceCmd cmd = new UpdateMPPriceCmd(productId, newPrice);

        MasterProduct product = new MasterProduct(
                new CategoryId(1),
                new MasterProductTitle("Product", "Model-1", new vn.ghtk.demo.catalog.domain.BrandId(100))
        );

        when(masterProductRepository.findProductById(productId)).thenReturn(product);

        // When
        masterProductUC.updateBasePrice(cmd);

        // Then
        assertEquals(newPrice, product.basePrice());
        verify(masterProductRepository).findProductById(productId);
        verify(masterProductRepository).save(product);
    }

    @Test
    void updateBasePrice_shouldThrowExceptionWhenProductNotFound() {
        // Given
        MasterProductId productId = new MasterProductId(999);
        Money newPrice = new Money(new BigDecimal("99.99"), vn.ghtk.demo.catalog.domain.Currency.USD);
        UpdateMPPriceCmd cmd = new UpdateMPPriceCmd(productId, newPrice);

        when(masterProductRepository.findProductById(productId)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundMPException.class, () -> masterProductUC.updateBasePrice(cmd));
        verify(masterProductRepository, never()).save(any(MasterProduct.class));
    }

    @Test
    void publishProduct_shouldPublishProduct() {
        // Given
        MasterProductId productId = new MasterProductId(1);
        MasterProduct product = new MasterProduct(
                new CategoryId(1),
                new MasterProductTitle("Product", "Model-1", new vn.ghtk.demo.catalog.domain.BrandId(100))
        );

        when(masterProductRepository.findProductById(productId)).thenReturn(product);

        // When
        masterProductUC.publishProduct(productId);

        // Then
        verify(masterProductRepository).findProductById(productId);
        verify(masterProductRepository).save(product);
    }

    @Test
    void publishProduct_shouldThrowExceptionWhenProductNotFound() {
        // Given
        MasterProductId productId = new MasterProductId(999);

        when(masterProductRepository.findProductById(productId)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundMPException.class, () -> masterProductUC.publishProduct(productId));
        verify(masterProductRepository, never()).save(any(MasterProduct.class));
    }

    @Test
    void retireProduct_shouldRetireProduct() {
        // Given
        MasterProductId productId = new MasterProductId(1);
        MasterProduct product = new MasterProduct(
                new CategoryId(1),
                new MasterProductTitle("Product", "Model-1", new vn.ghtk.demo.catalog.domain.BrandId(100))
        );

        when(masterProductRepository.findProductById(productId)).thenReturn(product);

        // When
        masterProductUC.retireProduct(productId);

        // Then
        verify(masterProductRepository).findProductById(productId);
        verify(masterProductRepository).save(product);
    }

    @Test
    void retireProduct_shouldThrowExceptionWhenProductNotFound() {
        // Given
        MasterProductId productId = new MasterProductId(999);

        when(masterProductRepository.findProductById(productId)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundMPException.class, () -> masterProductUC.retireProduct(productId));
        verify(masterProductRepository, never()).save(any(MasterProduct.class));
    }
}
