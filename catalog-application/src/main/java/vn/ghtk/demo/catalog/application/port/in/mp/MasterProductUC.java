package vn.ghtk.demo.catalog.application.port.in.mp;

import vn.ghtk.demo.catalog.application.port.in.mp.input.CreateProductCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.EditMasterProductCmd;
import vn.ghtk.demo.catalog.application.port.in.mp.input.UpdateMPPriceCmd;
import vn.ghtk.demo.catalog.application.port.out.mp.MasterProductRepository;
import vn.ghtk.demo.catalog.application.port.out.mp.SearchMasterProductCriteria;
import vn.ghtk.demo.catalog.common.exception.FoundedException;
import vn.ghtk.demo.catalog.domain.mp.MasterProduct;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.MasterProductTitle;
import vn.ghtk.demo.catalog.domain.mp.exception.NotFoundMPException;

public class MasterProductUC implements MasterProductIp {
    private final MasterProductRepository masterProductRepository;

    public MasterProductUC(MasterProductRepository masterProductRepository) {
        this.masterProductRepository = masterProductRepository;
    }

    @Override
    public MasterProductId createProduct(CreateProductCmd cmd) {
        SearchMasterProductCriteria criteria = new SearchMasterProductCriteria(cmd.brandId(), cmd.model());
        MasterProduct oldProduct = masterProductRepository.searchProduct(criteria);
        if (oldProduct != null) {
            throw new FoundedException("MasterProduct already exists, id = [" + oldProduct.id().id()+ "]");
        }

        MasterProduct newProduct = new MasterProduct(cmd.categoryId(),
                new MasterProductTitle(cmd.title(),cmd.model(), cmd.brandId()));
        masterProductRepository.save(newProduct);
        return newProduct.id();
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
