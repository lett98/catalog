package vn.ghtk.demo.catalog.adapter.facade.mp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.AttributeValueRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.CreateVariantRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.EditMasterVariantRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MVOperationResult;
import vn.ghtk.demo.catalog.application.port.in.mp.MasterVariantIp;
import vn.ghtk.demo.catalog.application.port.in.mp.input.AddMasterVariantCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterVariantCmd;
import vn.ghtk.demo.catalog.domain.attribute.AttributeId;
import vn.ghtk.demo.catalog.domain.attribute.AttributeValue;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantStatus;
import vn.ghtk.demo.catalog.domain.mp.VariantCodeType;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MasterVariantFacadeTest {

    @Mock
    private MasterVariantIp masterVariantIp;

    private MasterVariantFacade masterVariantFacade;

    @BeforeEach
    void setUp() {
        masterVariantFacade = new MasterVariantFacade(masterVariantIp);
    }

    @Test
    void createMasterVariant_shouldCreateVariantAndReturnResult() {
        // Given
        AttributeValueRequest attrValueReq = new AttributeValueRequest();
        attrValueReq.setAttributeId(1);
        attrValueReq.setValue("Red");

        CreateVariantRequest request = new CreateVariantRequest();
        request.setMasterProductId(123);
        request.setAttributeValues(List.of(attrValueReq));
        request.setName("Red Variant");
        request.setCodeValue("SKU-001");
        request.setCodeType(VariantCodeType.SKU);

        MasterVariantId newVariantId = new MasterVariantId(456);
        when(masterVariantIp.createVariant(any(AddMasterVariantCmd.class))).thenReturn(newVariantId);

        // When
        MVOperationResult result = masterVariantFacade.createMasterVariant(request);

        // Then
        assertNotNull(result);
        assertEquals(newVariantId.id(), result.getMasterVariantId());
        assertEquals(MasterVariantStatus.DRAFT.status(), result.getStatus());
        verify(masterVariantIp).createVariant(any(AddMasterVariantCmd.class));
    }

    @Test
    void createMasterVariant_shouldPassCorrectCommandToUseCase() {
        // Given
        AttributeValueRequest attrValueReq = new AttributeValueRequest();
        attrValueReq.setAttributeId(1);
        attrValueReq.setValue("Red");

        CreateVariantRequest request = new CreateVariantRequest();
        request.setMasterProductId(123);
        request.setAttributeValues(List.of(attrValueReq));
        request.setName("Red Variant");
        request.setCodeValue("SKU-001");
        request.setCodeType(VariantCodeType.SKU);

        ArgumentCaptor<AddMasterVariantCmd> cmdCaptor = ArgumentCaptor.forClass(AddMasterVariantCmd.class);
        when(masterVariantIp.createVariant(any(AddMasterVariantCmd.class)))
                .thenReturn(new MasterVariantId(456));

        // When
        masterVariantFacade.createMasterVariant(request);

        // Then
        verify(masterVariantIp).createVariant(cmdCaptor.capture());
        AddMasterVariantCmd cmd = cmdCaptor.getValue();
        assertEquals(123, cmd.masterProductId().id());
        assertEquals(1, cmd.attributeValues().size());
        assertEquals("Red Variant", cmd.name());
        assertEquals("SKU-001", cmd.codeValue());
        assertEquals(VariantCodeType.SKU, cmd.codeType());
    }

    @Test
    void editMasterVariant_shouldEditVariant() {
        // Given
        EditMasterVariantRequest request = new EditMasterVariantRequest();
        request.setMasterProductId(123);
        request.setMasterVariantId(456);
        request.setName("Updated Name");
        request.setImages(List.of("http://example.com/image.jpg"));

        // When
        masterVariantFacade.editMasterVariant(request);

        // Then
        verify(masterVariantIp).editVariant(any(EditMasterVariantCmd.class));
    }

    @Test
    void editMasterVariant_shouldHandleEmptyImages() {
        // Given
        EditMasterVariantRequest request = new EditMasterVariantRequest();
        request.setMasterProductId(123);
        request.setMasterVariantId(456);
        request.setName("Updated Name");
        request.setImages(Collections.emptyList());

        ArgumentCaptor<EditMasterVariantCmd> cmdCaptor = ArgumentCaptor.forClass(EditMasterVariantCmd.class);

        // When
        masterVariantFacade.editMasterVariant(request);

        // Then
        verify(masterVariantIp).editVariant(cmdCaptor.capture());
        EditMasterVariantCmd cmd = cmdCaptor.getValue();
        assertTrue(cmd.images().isEmpty());
    }

    @Test
    void editMasterVariant_shouldHandleNullImages() {
        // Given
        EditMasterVariantRequest request = new EditMasterVariantRequest();
        request.setMasterProductId(123);
        request.setMasterVariantId(456);
        request.setName("Updated Name");
        request.setImages(null);

        ArgumentCaptor<EditMasterVariantCmd> cmdCaptor = ArgumentCaptor.forClass(EditMasterVariantCmd.class);

        // When
        masterVariantFacade.editMasterVariant(request);

        // Then
        verify(masterVariantIp).editVariant(cmdCaptor.capture());
        EditMasterVariantCmd cmd = cmdCaptor.getValue();
        assertTrue(cmd.images().isEmpty());
    }

    @Test
    void publishMasterVariant_shouldPublishAndReturnResult() {
        // Given
        Integer masterProductId = 123;
        Integer masterVariantId = 456;

        // When
        MVOperationResult result = masterVariantFacade.publishMasterVariant(masterProductId, masterVariantId);

        // Then
        assertNotNull(result);
        assertEquals(masterVariantId, result.getMasterVariantId());
        assertEquals(MasterVariantStatus.PUBLISHED.status(), result.getStatus());
        verify(masterVariantIp).publishVariant(
                any(MasterProductId.class),
                any(MasterVariantId.class)
        );
    }

    @Test
    void publishMasterVariant_shouldPassCorrectIds() {
        // Given
        Integer masterProductId = 123;
        Integer masterVariantId = 456;

        ArgumentCaptor<MasterProductId> productIdCaptor = ArgumentCaptor.forClass(MasterProductId.class);
        ArgumentCaptor<MasterVariantId> variantIdCaptor = ArgumentCaptor.forClass(MasterVariantId.class);

        // When
        masterVariantFacade.publishMasterVariant(masterProductId, masterVariantId);

        // Then
        verify(masterVariantIp).publishVariant(productIdCaptor.capture(), variantIdCaptor.capture());
        assertEquals(masterProductId, productIdCaptor.getValue().id());
        assertEquals(masterVariantId, variantIdCaptor.getValue().id());
    }

    @Test
    void retireMasterVariant_shouldRetireAndReturnResult() {
        // Given
        Integer masterProductId = 123;
        Integer masterVariantId = 456;

        // When
        MVOperationResult result = masterVariantFacade.retireMasterVariant(masterProductId, masterVariantId);

        // Then
        assertNotNull(result);
        assertEquals(masterVariantId, result.getMasterVariantId());
        assertEquals(MasterVariantStatus.RETIRED.status(), result.getStatus());
        verify(masterVariantIp).retireVariant(
                any(MasterProductId.class),
                any(MasterVariantId.class)
        );
    }
}
