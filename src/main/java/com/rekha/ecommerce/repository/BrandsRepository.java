package com.rekha.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.Brands;

@Repository
public interface BrandsRepository extends JpaRepository<Brands, Long>, JpaSpecificationExecutor<Brands>{

	List<Brands> findByCategoryName(String category);

	Brands findByBrandNameAndCategoryName(String brandName, String categoryName);

}
