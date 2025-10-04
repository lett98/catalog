package vn.ghtk.demo.catalog.adapter.inbound.restful.mp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.ghtk.demo.catalog.adapter.facade.mp.ViewMasterVariantFacade;
import vn.ghtk.demo.catalog.adapter.inbound.restful.common.BaseResponse;
import vn.ghtk.demo.catalog.adapter.inbound.restful.common.ResponseFactory;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterVariantDto;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterVariantListDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/demo/mp/variant")
@Slf4j
public class MasterVariantController {
    private final ViewMasterVariantFacade viewMasterVariantFacade;

    @GetMapping()
    public ResponseEntity<BaseResponse<MasterVariantDto>> getMasterVariant(@RequestParam(name = "id") Integer masterVariantId) {
        try {
            MasterVariantDto response = viewMasterVariantFacade.findMasterVariantById(masterVariantId);
            return ResponseFactory.success(response);
        } catch (Exception e) {
            log.error("Error getMasterVariant {}", masterVariantId, e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<BaseResponse<List<MasterVariantListDto>>> getMasterVariants(@RequestParam(name = "product_id") Integer masterProductId) {
        try {
            List<MasterVariantListDto> response = viewMasterVariantFacade.findVariantsOfProduct(masterProductId);
            return ResponseFactory.success(response);
        } catch (Exception e) {
            log.error("Error getVariantsOfProduct {}", masterProductId, e);
            return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
