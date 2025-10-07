package vn.ghtk.demo.catalog.adapter.inbound.restful.mp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vn.ghtk.demo.catalog.adapter.facade.mp.MasterProductFacade;
import vn.ghtk.demo.catalog.adapter.facade.mp.ViewMasterProductFacade;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.CreateProductRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.EditMasterProductRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.UpdateMPPriceRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MPOperationResult;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterProductDto;
import vn.ghtk.demo.catalog.common.PagedResult;
import vn.ghtk.demo.catalog.common.exception.FoundedException;
import vn.ghtk.demo.catalog.domain.Currency;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductStatus;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMPException;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MasterProductController.class)
class MasterProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MasterProductFacade masterProductFacade;

    @MockBean
    private ViewMasterProductFacade viewMasterProductFacade;

    @Test
    void createMasterProduct_shouldReturnSuccess() throws Exception {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setCategoryId(1);
        request.setBrandId(100);
        request.setModel("Model-1");
        request.setTitle("Test Product");

        MPOperationResult result = new MPOperationResult(
                new MasterProductId(123),
                MasterProductStatus.DRAFT
        );

        when(masterProductFacade.createMasterProduct(any(CreateProductRequest.class)))
                .thenReturn(result);

        // When & Then
        mockMvc.perform(post("/api/demo/mp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.masterProductId").value(123))
                .andExpect(jsonPath("$.data.status").value((int) MasterProductStatus.DRAFT.status()));

        verify(masterProductFacade).createMasterProduct(any(CreateProductRequest.class));
    }

    @Test
    void createMasterProduct_shouldReturnErrorWhenProductExists() throws Exception {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setCategoryId(1);
        request.setBrandId(100);
        request.setModel("Model-1");
        request.setTitle("Test Product");

        when(masterProductFacade.createMasterProduct(any(CreateProductRequest.class)))
                .thenThrow(new FoundedException("Product already exists"));

        // When & Then
        mockMvc.perform(post("/api/demo/mp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Product already exists"));
    }

    @Test
    void editMasterProduct_shouldReturnSuccess() throws Exception {
        // Given
        EditMasterProductRequest request = new EditMasterProductRequest();
        request.setMasterProductId(123);
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setAttributeConfigs(Collections.emptyList());

        doNothing().when(masterProductFacade).editMasterProduct(any(EditMasterProductRequest.class));

        // When & Then
        mockMvc.perform(put("/api/demo/mp/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(masterProductFacade).editMasterProduct(any(EditMasterProductRequest.class));
    }

    @Test
    void editMasterProduct_shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        // Given
        EditMasterProductRequest request = new EditMasterProductRequest();
        request.setMasterProductId(999);
        request.setTitle("Title");

        doThrow(new NotFoundMPException(new MasterProductId(999)))
                .when(masterProductFacade).editMasterProduct(any(EditMasterProductRequest.class));

        // When & Then
        mockMvc.perform(put("/api/demo/mp/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void updateMPPrice_shouldReturnSuccess() throws Exception {
        // Given
        UpdateMPPriceRequest request = new UpdateMPPriceRequest();
        request.setMasterProductId(123);
        request.setPrice(new BigDecimal("99.99"));
        request.setCurrency(Currency.USD);

        doNothing().when(masterProductFacade).updateProductBasePrice(any(UpdateMPPriceRequest.class));

        // When & Then
        mockMvc.perform(put("/api/demo/mp/update-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(masterProductFacade).updateProductBasePrice(any(UpdateMPPriceRequest.class));
    }

    @Test
    void updateMPPrice_shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        // Given
        UpdateMPPriceRequest request = new UpdateMPPriceRequest();
        request.setMasterProductId(999);
        request.setPrice(new BigDecimal("99.99"));
        request.setCurrency(Currency.USD);

        doThrow(new NotFoundMPException(new MasterProductId(999)))
                .when(masterProductFacade).updateProductBasePrice(any(UpdateMPPriceRequest.class));

        // When & Then
        mockMvc.perform(put("/api/demo/mp/update-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void publishMasterProduct_shouldReturnSuccess() throws Exception {
        // Given
        Integer productId = 123;
        MPOperationResult result = new MPOperationResult(
                new MasterProductId(productId),
                MasterProductStatus.PUBLISHED
        );

        when(masterProductFacade.publishMasterProduct(anyInt())).thenReturn(result);

        // When & Then
        mockMvc.perform(put("/api/demo/mp/publish")
                        .param("id", productId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value((int) MasterProductStatus.PUBLISHED.status()));

        verify(masterProductFacade).publishMasterProduct(productId);
    }

    @Test
    void publishMasterProduct_shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        // Given
        Integer productId = 999;

        when(masterProductFacade.publishMasterProduct(anyInt()))
                .thenThrow(new NotFoundMPException(new MasterProductId(productId)));

        // When & Then
        mockMvc.perform(put("/api/demo/mp/publish")
                        .param("id", productId.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void retireMasterProduct_shouldReturnSuccess() throws Exception {
        // Given
        Integer productId = 123;
        MPOperationResult result = new MPOperationResult(
                new MasterProductId(productId),
                MasterProductStatus.RETIRED
        );

        when(masterProductFacade.retireMasterProduct(anyInt())).thenReturn(result);

        // When & Then
        mockMvc.perform(put("/api/demo/mp/retire")
                        .param("id", productId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value((int) MasterProductStatus.RETIRED.status()));

        verify(masterProductFacade).retireMasterProduct(productId);
    }

    @Test
    void getMasterProductById_shouldReturnSuccess() throws Exception {
        // Given
        Integer productId = 123;
        MasterProductDto productDto = mock(MasterProductDto.class);

        when(viewMasterProductFacade.findMasterProductById(anyInt())).thenReturn(productDto);

        // When & Then
        mockMvc.perform(get("/api/demo/mp/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(viewMasterProductFacade).findMasterProductById(productId);
    }

    @Test
    void getMasterProducts_shouldReturnPagedResults() throws Exception {
        // Given
        PagedResult pagedResult = mock(PagedResult.class);

        when(viewMasterProductFacade.listMasterProducts(anyInt(), anyInt(), any(), any(), any()))
                .thenReturn(pagedResult);

        // When & Then
        mockMvc.perform(get("/api/demo/mp")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(viewMasterProductFacade).listMasterProducts(1, 10, null, null, null);
    }
}
