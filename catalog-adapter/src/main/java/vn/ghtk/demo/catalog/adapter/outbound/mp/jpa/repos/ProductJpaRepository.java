package vn.ghtk.demo.catalog.adapter.outbound.mp.jpa.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ghtk.demo.catalog.adapter.outbound.mp.entity.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
