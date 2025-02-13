package com.rekha.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long>, JpaSpecificationExecutor<Products> {

	List<Products> findByBrandName(String brand);

	@Query(value = "select * from tb_products where product_id IN :prodIds", nativeQuery = true)
	List<Products> getProductsByIds(List<Long> prodIds);

}
