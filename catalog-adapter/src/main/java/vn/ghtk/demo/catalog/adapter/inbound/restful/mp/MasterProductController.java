package vn.ghtk.demo.catalog.adapter.inbound.restful.mp;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.ghtk.demo.catalog.adapter.facade.mp.ViewMasterProductFacade;
import vn.ghtk.demo.catalog.adapter.inbound.restful.common.BaseResponse;
import vn.ghtk.demo.catalog.adapter.inbound.restful.common.ResponseFactory;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterProductDto;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MasterProductListDto;
import vn.ghtk.demo.catalog.common.PagedResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/demo/mp")
@Slf4j
public class MasterProductController {
    private final ViewMasterProductFacade viewMasterProductFacade;

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
}
