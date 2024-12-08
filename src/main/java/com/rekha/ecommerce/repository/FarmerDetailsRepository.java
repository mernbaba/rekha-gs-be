package com.rekha.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.FarmerDetails;

@Repository
public interface FarmerDetailsRepository
		extends JpaRepository<FarmerDetails, Long>, JpaSpecificationExecutor<FarmerDetails> {

}
