package com.rekha.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.UserAddressDetails;

@Repository
public interface UserAddressDetailsRepository
		extends JpaRepository<UserAddressDetails, Long>, JpaSpecificationExecutor<UserAddressDetails> {

	List<UserAddressDetails> findByPhoneNumber(String phoneNumber);

}
