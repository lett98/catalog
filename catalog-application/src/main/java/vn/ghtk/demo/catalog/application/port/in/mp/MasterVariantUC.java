package vn.ghtk.demo.catalog.application.port.in.mp;

import vn.ghtk.demo.catalog.application.port.in.mp.input.AddMasterVariantCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterVariantCmd;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductRepository;
import vn.ghtk.demo.catalog.domain.mp.MasterProduct;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterVariant;
import vn.ghtk.demo.catalog.domain.mp.MasterVariantId;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMPException;

public class MasterVariantUC implements MasterVariantIp {
    private final MasterProductRepository masterProductRepository;

    public MasterVariantUC(MasterProductRepository masterProductRepository) {
        this.masterProductRepository = masterProductRepository;
    }

    @Override
    public MasterVariantId createVariant(AddMasterVariantCmd cmd) {
        MasterProduct masterProduct = this.findMasterProduct(cmd.masterProductId());
        MasterVariant newVariant = masterProduct.addVariant(cmd.attributeValues(),
                cmd.name(),
                cmd.codeValue(),
                cmd.codeType());
        masterProductRepository.save(masterProduct);
        return newVariant.id();
    }

    @Override
    public void editVariant(EditMasterVariantCmd cmd) {
        MasterProduct masterProduct = this.findMasterProduct(cmd.masterProductId());
        masterProduct.editVariant(cmd.buildEditProductVariantSpec());
        masterProductRepository.save(masterProduct);
    }

    @Override
    public void publishVariant(MasterProductId masterProductId, MasterVariantId masterVariantId) {
        MasterProduct masterProduct = this.findMasterProduct(masterProductId);
        masterProduct.publishVariant(masterVariantId);
        masterProductRepository.save(masterProduct);
    }

    @Override
    public void retireVariant(MasterProductId masterProductId, MasterVariantId masterVariantId) {
        MasterProduct masterProduct = this.findMasterProduct(masterProductId);
        masterProduct.retireVariant(masterVariantId);
        masterProductRepository.save(masterProduct);
    }

    private MasterProduct findMasterProduct(MasterProductId masterProductId) {
        MasterProduct masterProduct = masterProductRepository.findProductById(masterProductId);
        if (masterProduct == null) {
            throw new NotFoundMPException(masterProductId);
        }
        return masterProduct;
    }
}
