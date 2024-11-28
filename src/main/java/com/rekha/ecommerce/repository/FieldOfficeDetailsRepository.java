package com.rekha.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.FieldOfficerDetails;

@Repository
public interface FieldOfficeDetailsRepository
		extends JpaRepository<FieldOfficerDetails, Long>, JpaSpecificationExecutor<FieldOfficerDetails> {

	FieldOfficerDetails findByPhoneNumber(String phoneNumber);

}
