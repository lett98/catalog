package vn.ghtk.demo.catalog.adapter.inbound.restful.mp;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ghtk.demo.catalog.adapter.facade.mp.MasterProductFacade;
import vn.ghtk.demo.catalog.adapter.facade.mp.ViewMasterProductFacade;
import vn.ghtk.demo.catalog.adapter.inbound.restful.common.BaseResponse;
import vn.ghtk.demo.catalog.adapter.inbound.restful.common.ResponseFactory;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.CreateProductRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.EditMasterProductRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.UpdateMPPriceRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MPOperationResult;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterProductDto;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterProductListDto;
import vn.ghtk.demo.catalog.common.PagedResult;
import vn.ghtk.demo.catalog.common.exception.FoundedException;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMPException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/demo/mp")
@Slf4j
public class MasterProductController {
    private final ViewMasterProductFacade viewMasterProductFacade;
    private final MasterProductFacade masterProductFacade;

    @GetMapping()
    public ResponseEntity<BaseResponse<PagedResult<MasterProductListDto>>> getMasterProducts(@RequestParam(name = "page", defaultValue = "1") @Valid @Min(1)  int page,
                                                                                @RequestParam(name = "size", defaultValue = "10") @Valid @Max(100) @Min(1) int size,
                                                                                @RequestParam(name = "sort_by", required = false) List<String> sortFields,
                                                                                @RequestParam(name = "category_id", required = false) Integer categoryId,
                                                                                @RequestParam(name = "status", required = false) Short status) {
        try {
            PagedResult<MasterProductListDto> response = viewMasterProductFacade.listMasterProducts(page, size, sortFields, categoryId, status);
            return ResponseFactory.success(response);
        } catch (Exception e) {
            log.error("Error getMasterProducts page {}, size {}", page, size, e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<BaseResponse<MasterProductDto>> getMasterProducts(@RequestParam(name = "id") Integer masterProductId) {
        try {
            MasterProductDto response = viewMasterProductFacade.findMasterProductById(masterProductId);
            return ResponseFactory.success(response);
        } catch (Exception e) {
            log.error("Error getMasterProduct {}", masterProductId, e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<BaseResponse<MPOperationResult>> createMasterProduct(@RequestBody CreateProductRequest request) {
        try {
            MPOperationResult response = masterProductFacade.createMasterProduct(request);
            return ResponseFactory.success(response);
        } catch (FoundedException e) {
            return ResponseFactory.error(HttpStatus.FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error createMasterProduct", e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<BaseResponse<Void>> editMasterProduct(@RequestBody EditMasterProductRequest request) {
        try {
            masterProductFacade.editMasterProduct(request);
            return ResponseFactory.success(null);
        } catch (NotFoundMPException e) {
            return ResponseFactory.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error editMasterProduct {}", request.getMasterProductId(), e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-price")
    public ResponseEntity<BaseResponse<Void>> updateMPPrice(@RequestBody UpdateMPPriceRequest request) {
        try {
            masterProductFacade.updateProductBasePrice(request);
            return ResponseFactory.success(null);
        } catch (NotFoundMPException e) {
            return ResponseFactory.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error updateMasterProductPrice {}", request.getMasterProductId(), e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/publish")
    public ResponseEntity<BaseResponse<MPOperationResult>> publishMasterProduct(@RequestParam(name = "id") Integer masterProductId) {
        try {
            MPOperationResult response =  masterProductFacade.publishMasterProduct(masterProductId);
            return ResponseFactory.success(response);
        } catch (NotFoundMPException e) {
            return ResponseFactory.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error publishMasterProduct {}", masterProductId, e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/retire")
    public ResponseEntity<BaseResponse<MPOperationResult>> retireMasterProduct(@RequestParam(name = "id") Integer masterProductId) {
        try {
            MPOperationResult response =  masterProductFacade.retireMasterProduct(masterProductId);
            return ResponseFactory.success(response);
        } catch (NoSuchElementException e) {
            return ResponseFactory.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error retireMasterProduct {}", masterProductId, e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
