package vn.ghtk.demo.catalog.adapter.facade.mp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.AttributeValueRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.CreateVariantRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.request.EditMasterVariantRequest;
import vn.ghtk.demo.catalog.adapter.inbound.restful.mp.response.MVOperationResult;
import vn.ghtk.demo.catalog.application.port.in.mp.MasterVariantIp;
import vn.ghtk.demo.catalog.application.port.in.mp.input.AddMasterVariantCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterVariantCmd;
import vn.ghtk.demo.catalog.domain.ImageUrl;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantName;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantStatus;

import java.util.Collections;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class MasterVariantFacade {
    private final MasterVariantIp masterVariantIp;

    @Transactional
    public MVOperationResult createMasterVariant(CreateVariantRequest request) {
        AddMasterVariantCmd cmd = new AddMasterVariantCmd(
                new MasterProductId(request.getMasterProductId()),
                request.getAttributeValues().stream().map(AttributeValueRequest::attributeValue).toList(),
                request.getName(),
                request.getCodeValue(),
                request.getCodeType());
        MasterVariantId newVariantId = masterVariantIp.createVariant(cmd);
        return new MVOperationResult(newVariantId, MasterVariantStatus.DRAFT);
    }

    @Transactional
    public void editMasterVariant(EditMasterVariantRequest request) {
        EditMasterVariantCmd cmd = new EditMasterVariantCmd(
                new MasterProductId(request.getMasterProductId()),
                new MasterVariantId(request.getMasterVariantId()),
                (Objects.isNull(request.getImages()) || request.getImages().isEmpty()) ?
                        Collections.emptyList() :
                        request.getImages().stream().map(ImageUrl::new).toList(),
                new MasterVariantName(request.getName()));
        masterVariantIp.editVariant(cmd);
    }

    @Transactional
    public MVOperationResult publishMasterVariant(Integer masterProductId, Integer masterVariantId) {
        masterVariantIp.publishVariant(new MasterProductId(masterProductId), new MasterVariantId(masterVariantId));
        return new MVOperationResult(new MasterVariantId(masterVariantId), MasterVariantStatus.PUBLISHED);
    }

    @Transactional
    public MVOperationResult retireMasterVariant(Integer masterProductId, Integer masterVariantId) {
        masterVariantIp.retireVariant(new MasterProductId(masterProductId), new MasterVariantId(masterVariantId));
        return new MVOperationResult(new MasterVariantId(masterVariantId), MasterVariantStatus.RETIRED);
    }



}
