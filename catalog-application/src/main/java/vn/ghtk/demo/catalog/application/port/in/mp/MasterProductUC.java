package vn.ghtk.demo.catalog.application.port.in.mp;

import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterProductCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.UpdateMPPriceCmd;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductRepository;
import vn.ghtk.demo.catalog.domain.mp.MasterProduct;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMPException;

public class MasterProductUC implements MasterProductIp {
    private final MasterProductRepository masterProductRepository;

    public MasterProductUC(MasterProductRepository masterProductRepository) {
        this.masterProductRepository = masterProductRepository;
    }

    @Override
    public void editProduct(EditMasterProductCmd cmd) {
        MasterProduct masterProduct = this.findMasterProduct(cmd.masterProductId());
        masterProduct.editProduct(cmd.buildProductSpec());
        masterProductRepository.save(masterProduct);
    }

    @Override
    public void updateBasePrice(UpdateMPPriceCmd cmd) {
        MasterProduct masterProduct = this.findMasterProduct(cmd.masterProductId());
        masterProduct.updateBasePrice(cmd.newPrice());
        masterProductRepository.save(masterProduct);
    }

    @Override
    public void publishProduct(MasterProductId masterProductId) {
        MasterProduct masterProduct = this.findMasterProduct(masterProductId);
        masterProduct.publishProduct();
        masterProductRepository.save(masterProduct);
    }

    @Override
    public void retireProduct(MasterProductId masterProductId) {
        MasterProduct masterProduct = this.findMasterProduct(masterProductId);
        masterProduct.retireProduct();
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
