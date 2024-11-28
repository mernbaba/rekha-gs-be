package com.rekha.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.ProductImages;

@Repository
public interface ProductImagesRepository
		extends JpaRepository<ProductImages, Long>, JpaSpecificationExecutor<ProductImages> {

	@Query(value = "select * from tb_product_images where product_id in :productIds", nativeQuery = true)
	List<ProductImages> getImagesByProductIds(@Param("productIds") List<Long> productIds);

}
