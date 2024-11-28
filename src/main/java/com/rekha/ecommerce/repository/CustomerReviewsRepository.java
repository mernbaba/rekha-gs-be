package com.rekha.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rekha.ecommerce.entity.CustomerReviews;

public interface CustomerReviewsRepository
		extends JpaRepository<CustomerReviews, Long>, JpaSpecificationExecutor<CustomerReviews> {

}
