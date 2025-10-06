package vn.ghtk.demo.catalog.adapter.inbound.restful.mp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vn.ghtk.demo.catalog.adapter.facade.mp.MasterVariantFacade;
import vn.ghtk.demo.catalog.adapter.facade.mp.ViewMasterVariantFacade;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.AttributeValueRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.CreateVariantRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.EditMasterVariantRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MVOperationResult;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterVariantDto;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterVariantListDto;
import vn.ghtk.demo.catalog.common.exception.FoundedException;
import vn.ghtk.demo.catalog.domain.attribute.AttributeId;
import vn.ghtk.demo.catalog.domain.attribute.AttributeValue;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantStatus;
import vn.ghtk.demo.catalog.domain.mp.VariantCodeType;
import vn.ghtk.demo.catalog.domain.mp.exception.InvalidMasterProductState;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMPException;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMVException;
import vn.ghtk.demo.catalog.domain.mp.exception.VariantRequiredAttributeException;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MasterVariantController.class)
class MasterVariantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MasterVariantFacade masterVariantFacade;

    @MockBean
    private ViewMasterVariantFacade viewMasterVariantFacade;

    @Test
    void addProductVariant_shouldReturnSuccess() throws Exception {
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

        MVOperationResult result = new MVOperationResult(
                new MasterVariantId(456),
                MasterVariantStatus.DRAFT
        );

        when(masterVariantFacade.createMasterVariant(any(CreateVariantRequest.class)))
                .thenReturn(result);

        // When & Then
        mockMvc.perform(post("/api/demo/mp/variant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.masterVariantId").value(456))
                .andExpect(jsonPath("$.data.status").value((int) MasterVariantStatus.DRAFT.status()));

        verify(masterVariantFacade).createMasterVariant(any(CreateVariantRequest.class));
    }

    @Test
    void addProductVariant_shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        // Given
        CreateVariantRequest request = new CreateVariantRequest();
        request.setMasterProductId(999);
        request.setAttributeValues(Collections.emptyList());
        request.setName("Variant");
        request.setCodeValue("SKU-001");
        request.setCodeType(VariantCodeType.SKU);

        when(masterVariantFacade.createMasterVariant(any(CreateVariantRequest.class)))
                .thenThrow(new NotFoundMPException(new MasterProductId(999)));

        // When & Then
        mockMvc.perform(post("/api/demo/mp/variant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void addProductVariant_shouldReturnErrorWhenVariantAlreadyExists() throws Exception {
        // Given
        CreateVariantRequest request = new CreateVariantRequest();
        request.setMasterProductId(123);
        request.setAttributeValues(Collections.emptyList());
        request.setName("Variant");
        request.setCodeValue("SKU-001");
        request.setCodeType(VariantCodeType.SKU);

        when(masterVariantFacade.createMasterVariant(any(CreateVariantRequest.class)))
                .thenThrow(new FoundedException("Variant already exists"));

        // When & Then
        mockMvc.perform(post("/api/demo/mp/variant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void addProductVariant_shouldReturnErrorWhenRequiredAttributesMissing() throws Exception {
        // Given
        CreateVariantRequest request = new CreateVariantRequest();
        request.setMasterProductId(123);
        request.setAttributeValues(Collections.emptyList());
        request.setName("Variant");
        request.setCodeValue("SKU-001");
        request.setCodeType(VariantCodeType.SKU);

        when(masterVariantFacade.createMasterVariant(any(CreateVariantRequest.class)))
                .thenThrow(new VariantRequiredAttributeException("Missing required attributes"));

        // When & Then
        mockMvc.perform(post("/api/demo/mp/variant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void editMasterVariant_shouldReturnSuccess() throws Exception {
        // Given
        EditMasterVariantRequest request = new EditMasterVariantRequest();
        request.setMasterProductId(123);
        request.setMasterVariantId(456);
        request.setName("Updated Name");
        request.setImages(List.of("http://example.com/image.jpg"));

        doNothing().when(masterVariantFacade).editMasterVariant(any(EditMasterVariantRequest.class));

        // When & Then
        mockMvc.perform(put("/api/demo/mp/variant/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(masterVariantFacade).editMasterVariant(any(EditMasterVariantRequest.class));
    }

    @Test
    void editMasterVariant_shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        // Given
        EditMasterVariantRequest request = new EditMasterVariantRequest();
        request.setMasterProductId(999);
        request.setMasterVariantId(456);
        request.setName("Name");

        doThrow(new NotFoundMPException(new MasterProductId(999)))
                .when(masterVariantFacade).editMasterVariant(any(EditMasterVariantRequest.class));

        // When & Then
        mockMvc.perform(put("/api/demo/mp/variant/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void editMasterVariant_shouldReturnNotFoundWhenVariantDoesNotExist() throws Exception {
        // Given
        EditMasterVariantRequest request = new EditMasterVariantRequest();
        request.setMasterProductId(123);
        request.setMasterVariantId(999);
        request.setName("Name");

        doThrow(new NotFoundMVException(new MasterProductId(123), new MasterVariantId(999)))
                .when(masterVariantFacade).editMasterVariant(any(EditMasterVariantRequest.class));

        // When & Then
        mockMvc.perform(put("/api/demo/mp/variant/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void publishMasterVariant_shouldReturnSuccess() throws Exception {
        // Given
        Integer productId = 123;
        Integer variantId = 456;

        MVOperationResult result = new MVOperationResult(
                new MasterVariantId(variantId),
                MasterVariantStatus.PUBLISHED
        );

        when(masterVariantFacade.publishMasterVariant(anyInt(), anyInt())).thenReturn(result);

        // When & Then
        mockMvc.perform(put("/api/demo/mp/variant/publish")
                        .param("product_id", productId.toString())
                        .param("variant_id", variantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value((int) MasterVariantStatus.PUBLISHED.status()));

        verify(masterVariantFacade).publishMasterVariant(productId, variantId);
    }

    @Test
    void publishMasterVariant_shouldReturnErrorWhenProductNotPublished() throws Exception {
        // Given
        Integer productId = 123;
        Integer variantId = 456;

        when(masterVariantFacade.publishMasterVariant(anyInt(), anyInt()))
                .thenThrow(new InvalidMasterProductState(new MasterProductId(productId), null));

        // When & Then
        mockMvc.perform(put("/api/demo/mp/variant/publish")
                        .param("product_id", productId.toString())
                        .param("variant_id", variantId.toString()))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void retireMasterVariant_shouldReturnSuccess() throws Exception {
        // Given
        Integer productId = 123;
        Integer variantId = 456;

        MVOperationResult result = new MVOperationResult(
                new MasterVariantId(variantId),
                MasterVariantStatus.RETIRED
        );

        when(masterVariantFacade.retireMasterVariant(anyInt(), anyInt())).thenReturn(result);

        // When & Then
        mockMvc.perform(put("/api/demo/mp/variant/retire")
                        .param("product_id", productId.toString())
                        .param("variant_id", variantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value((int) MasterVariantStatus.RETIRED.status()));

        verify(masterVariantFacade).retireMasterVariant(productId, variantId);
    }

    @Test
    void getMasterVariant_shouldReturnSuccess() throws Exception {
        // Given
        Integer variantId = 456;
        MasterVariantDto variantDto = mock(MasterVariantDto.class);

        when(viewMasterVariantFacade.findMasterVariantById(anyInt())).thenReturn(variantDto);

        // When & Then
        mockMvc.perform(get("/api/demo/mp/variant/" + variantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(viewMasterVariantFacade).findMasterVariantById(variantId);
    }

    @Test
    void getMasterVariants_shouldReturnProductVariants() throws Exception {
        // Given
        Integer productId = 123;
        List<MasterVariantListDto> variants = List.of(mock(MasterVariantListDto.class));

        when(viewMasterVariantFacade.findVariantsOfProduct(anyInt())).thenReturn(variants);

        // When & Then
        mockMvc.perform(get("/api/demo/mp/variant/product/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(viewMasterVariantFacade).findVariantsOfProduct(productId);
    }
}
