package com.rekha.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.ProductQuantity;

@Repository
public interface ProductQuantityRepository
		extends JpaRepository<ProductQuantity, Long>, JpaSpecificationExecutor<ProductQuantity> {

	@Query(value = "select * from tb_product_quantity where product_id in :productIds", nativeQuery = true)
	List<ProductQuantity> getProductQuantitesByProductIds(List<Long> productIds);

}
