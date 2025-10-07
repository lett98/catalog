package vn.ghtk.demo.catalog.adapter.facade.mp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.AttributeConfigRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.CreateProductRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.EditMasterProductRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.UpdateMPPriceRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MPOperationResult;
import vn.ghtk.demo.catalog.application.port.in.mp.MasterProductIp;
import vn.ghtk.demo.catalog.application.port.in.mp.input.CreateProductCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterProductCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.UpdateMPPriceCmd;
import vn.ghtk.demo.catalog.domain.Currency;
import vn.ghtk.demo.catalog.domain.attribute.AttributeConfig;
import vn.ghtk.demo.catalog.domain.attribute.AttributeId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MasterProductFacadeTest {

    @Mock
    private MasterProductIp masterProductIp;

    private MasterProductFacade masterProductFacade;

    @BeforeEach
    void setUp() {
        masterProductFacade = new MasterProductFacade(masterProductIp);
    }

    @Test
    void createMasterProduct_shouldCreateProductAndReturnResult() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setCategoryId(1);
        request.setBrandId(100);
        request.setModel("Model-1");
        request.setTitle("Test Product");

        MasterProductId newProductId = new MasterProductId(123);
        when(masterProductIp.createProduct(any(CreateProductCmd.class))).thenReturn(newProductId);

        // When
        MPOperationResult result = masterProductFacade.createMasterProduct(request);

        // Then
        assertNotNull(result);
        assertEquals(newProductId.id(), result.getMasterProductId());
        assertEquals(MasterProductStatus.DRAFT.status(), result.getStatus());
        verify(masterProductIp).createProduct(any(CreateProductCmd.class));
    }

    @Test
    void createMasterProduct_shouldPassCorrectCommandToUseCase() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setCategoryId(1);
        request.setBrandId(100);
        request.setModel("Model-1");
        request.setTitle("Test Product");

        ArgumentCaptor<CreateProductCmd> cmdCaptor = ArgumentCaptor.forClass(CreateProductCmd.class);
        when(masterProductIp.createProduct(any(CreateProductCmd.class)))
                .thenReturn(new MasterProductId(123));

        // When
        masterProductFacade.createMasterProduct(request);

        // Then
        verify(masterProductIp).createProduct(cmdCaptor.capture());
        CreateProductCmd cmd = cmdCaptor.getValue();
        assertEquals(1, cmd.categoryId().id());
        assertEquals(100, cmd.brandId().id());
        assertEquals("Model-1", cmd.model());
        assertEquals("Test Product", cmd.title());
    }

    @Test
    void editMasterProduct_shouldEditProduct() {
        // Given
        EditMasterProductRequest request = new EditMasterProductRequest();
        request.setMasterProductId(123);
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setAttributeConfigs(Collections.emptyList());

        // When
        masterProductFacade.editMasterProduct(request);

        // Then
        verify(masterProductIp).editProduct(any(EditMasterProductCmd.class));
    }

    @Test
    void editMasterProduct_shouldMapAttributeConfigs() {
        // Given
        AttributeConfigRequest attrConfigReq = new AttributeConfigRequest();
        attrConfigReq.setAttributeId(1);
        attrConfigReq.setMandatory(true);

        EditMasterProductRequest request = new EditMasterProductRequest();
        request.setMasterProductId(123);
        request.setTitle("Title");
        request.setDescription("Description");
        request.setAttributeConfigs(List.of(attrConfigReq));

        ArgumentCaptor<EditMasterProductCmd> cmdCaptor = ArgumentCaptor.forClass(EditMasterProductCmd.class);

        // When
        masterProductFacade.editMasterProduct(request);

        // Then
        verify(masterProductIp).editProduct(cmdCaptor.capture());
        EditMasterProductCmd cmd = cmdCaptor.getValue();
        assertEquals(1, cmd.attributeConfigs().size());
    }

    @Test
    void updateProductBasePrice_shouldUpdatePrice() {
        // Given
        UpdateMPPriceRequest request = new UpdateMPPriceRequest();
        request.setMasterProductId(123);
        request.setPrice(new BigDecimal("99.99"));
        request.setCurrency(Currency.USD);

        // When
        masterProductFacade.updateProductBasePrice(request);

        // Then
        verify(masterProductIp).updateBasePrice(any(UpdateMPPriceCmd.class));
    }

    @Test
    void updateProductBasePrice_shouldPassCorrectMoneyObject() {
        // Given
        UpdateMPPriceRequest request = new UpdateMPPriceRequest();
        request.setMasterProductId(123);
        request.setPrice(new BigDecimal("99.99"));
        request.setCurrency(Currency.USD);

        ArgumentCaptor<UpdateMPPriceCmd> cmdCaptor = ArgumentCaptor.forClass(UpdateMPPriceCmd.class);

        // When
        masterProductFacade.updateProductBasePrice(request);

        // Then
        verify(masterProductIp).updateBasePrice(cmdCaptor.capture());
        UpdateMPPriceCmd cmd = cmdCaptor.getValue();
        assertEquals(new BigDecimal("99.99"), cmd.newPrice().amount());
        assertEquals(Currency.USD, cmd.newPrice().currency());
    }

    @Test
    void publishMasterProduct_shouldPublishAndReturnResult() {
        // Given
        Integer masterProductId = 123;

        // When
        MPOperationResult result = masterProductFacade.publishMasterProduct(masterProductId);

        // Then
        assertNotNull(result);
        assertEquals(masterProductId, result.getMasterProductId());
        assertEquals(MasterProductStatus.PUBLISHED.status(), result.getStatus());
        verify(masterProductIp).publishProduct(any(MasterProductId.class));
    }

    @Test
    void retireMasterProduct_shouldRetireAndReturnResult() {
        // Given
        Integer masterProductId = 123;

        // When
        MPOperationResult result = masterProductFacade.retireMasterProduct(masterProductId);

        // Then
        assertNotNull(result);
        assertEquals(masterProductId, result.getMasterProductId());
        assertEquals(MasterProductStatus.RETIRED.status(), result.getStatus());
        verify(masterProductIp).retireProduct(any(MasterProductId.class));
    }
}
