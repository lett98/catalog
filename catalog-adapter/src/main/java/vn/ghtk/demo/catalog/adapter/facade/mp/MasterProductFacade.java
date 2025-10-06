package vn.ghtk.demo.catalog.adapter.facade.mp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.AttributeConfigRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.CreateProductRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.EditMasterProductRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.UpdateMPPriceRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MPOperationResult;
import vn.ghtk.demo.catalog.application.port.in.mp.MasterProductIp;
import vn.ghtk.demo.catalog.application.port.in.mp.input.CreateProductCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterProductCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.UpdateMPPriceCmd;
import vn.ghtk.demo.catalog.domain.BrandId;
import vn.ghtk.demo.catalog.domain.Money;
import vn.ghtk.demo.catalog.domain.category.CategoryId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductStatus;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class MasterProductFacade {
    private final MasterProductIp masterProductIp;

    @Transactional
    public MPOperationResult createMasterProduct(CreateProductRequest request) {
        CreateProductCmd cmd = new CreateProductCmd(new CategoryId(request.getCategoryId()),
                new BrandId(request.getBrandId()),
                request.getModel(),
                request.getTitle());
        MasterProductId newProductId = masterProductIp.createProduct(cmd);
        return new MPOperationResult(newProductId, MasterProductStatus.DRAFT);
    }

    @Transactional
    public void editMasterProduct(EditMasterProductRequest request) {
        EditMasterProductCmd cmd = new EditMasterProductCmd(
                new MasterProductId(request.getMasterProductId()),
                request.getDescription(),
                request.getTitle(),
                request.getAttributeConfigs().isEmpty() ? Collections.emptyList() :
                        request.getAttributeConfigs().stream().map(AttributeConfigRequest::attributeConfig).toList()
        );
        masterProductIp.editProduct(cmd);
    }

    @Transactional
    public void updateProductBasePrice(UpdateMPPriceRequest request) {
        UpdateMPPriceCmd cmd = new UpdateMPPriceCmd(
                new MasterProductId(request.getMasterProductId()),
                new Money(request.getPrice(), request.getCurrency()));
        masterProductIp.updateBasePrice(cmd);
    }

    @Transactional
    public MPOperationResult publishMasterProduct(Integer masterProductId) {
        masterProductIp.publishProduct(new MasterProductId(masterProductId));
        return new MPOperationResult(new MasterProductId(masterProductId), MasterProductStatus.PUBLISHED);
    }

    @Transactional
    public MPOperationResult retireMasterProduct(Integer masterProductId) {
        masterProductIp.retireProduct(new MasterProductId(masterProductId));
        return new MPOperationResult(new MasterProductId(masterProductId), MasterProductStatus.RETIRED);
    }


}
