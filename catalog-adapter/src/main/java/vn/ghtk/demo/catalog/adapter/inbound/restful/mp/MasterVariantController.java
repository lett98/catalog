package vn.ghtk.demo.catalog.adapter.inbound.restful.mp;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ghtk.demo.catalog.adapter.facade.mp.MasterVariantFacade;
import vn.ghtk.demo.catalog.adapter.facade.mp.ViewMasterVariantFacade;
import vn.ghtk.demo.catalog.adapter.inbound.restful.common.BaseResponse;
import vn.ghtk.demo.catalog.adapter.inbound.restful.common.ResponseFactory;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.CreateVariantRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.EditMasterProductRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.EditMasterVariantRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MVOperationResult;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterVariantDto;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterVariantListDto;
import vn.ghtk.demo.catalog.common.exception.FoundedException;
import vn.ghtk.demo.catalog.domain.mp.exception.VariantRequiredAttributeException;
import vn.ghtk.demo.catalog.domain.mp.exception.InvalidMasterProductState;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMPException;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMVException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/demo/mp/variant")
@Slf4j
public class MasterVariantController {
    private final ViewMasterVariantFacade viewMasterVariantFacade;
    private final MasterVariantFacade masterVariantFacade;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<MasterVariantDto>> getMasterVariant(@PathVariable("id") @NotNull Integer masterVariantId) {
        try {
            MasterVariantDto response = viewMasterVariantFacade.findMasterVariantById(masterVariantId);
            return ResponseFactory.success(response);
        } catch (Exception e) {
            log.error("Error getMasterVariant {}", masterVariantId, e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{product_id}")
    public ResponseEntity<BaseResponse<List<MasterVariantListDto>>> getMasterVariants(@PathVariable("product_id") @NotNull Integer masterProductId) {
        try {
            List<MasterVariantListDto> response = viewMasterVariantFacade.findVariantsOfProduct(masterProductId);
            return ResponseFactory.success(response);
        } catch (Exception e) {
            log.error("Error getVariantsOfProduct {}", masterProductId, e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<BaseResponse<MVOperationResult>> addProductVariant(@RequestBody CreateVariantRequest request) {
        try {
            MVOperationResult response = masterVariantFacade.createMasterVariant(request);
            return ResponseFactory.success(response);
        } catch (NotFoundMPException e) {
            return ResponseFactory.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (FoundedException e) {
            return ResponseFactory.error(HttpStatus.FOUND, e.getMessage());
        } catch (VariantRequiredAttributeException e) {
            return ResponseFactory.error(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (Exception e) {
            log.error("Error addVariant for MasterProduct {}", request.getMasterProductId(), e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<BaseResponse<Void>> editMasterVariant(@RequestBody EditMasterVariantRequest request) {
        try {
            masterVariantFacade.editMasterVariant(request);
            return ResponseFactory.success(null);
        } catch (NotFoundMPException | NotFoundMVException e) {
            return ResponseFactory.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error editMasterVariant {}", request.getMasterVariantId(), e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/publish")
    public ResponseEntity<BaseResponse<MVOperationResult>> publishMasterVariant(@RequestParam(name = "product_id") @NotNull Integer masterProductId,
                                                                                @RequestParam(name = "variant_id") @NotNull Integer masterVariantId) {
        try {
            MVOperationResult response =  masterVariantFacade.publishMasterVariant(masterProductId, masterVariantId);
            return ResponseFactory.success(response);
        } catch (NotFoundMPException | NotFoundMVException e) {
            return ResponseFactory.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidMasterProductState e) {
            return ResponseFactory.error(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (Exception e) {
            log.error("Error publishMasterVariant {}", masterProductId, e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/retire")
    public ResponseEntity<BaseResponse<MVOperationResult>> retireMasterVariant(@RequestParam(name = "product_id") @NotNull Integer masterProductId,
                                                                               @RequestParam(name = "variant_id") @NotNull Integer masterVariantId) {
        try {
            MVOperationResult response =  masterVariantFacade.retireMasterVariant(masterProductId, masterVariantId);
            return ResponseFactory.success(response);
        } catch (NotFoundMPException | NotFoundMVException e) {
            return ResponseFactory.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error retireMasterVariant {}", masterProductId, e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
