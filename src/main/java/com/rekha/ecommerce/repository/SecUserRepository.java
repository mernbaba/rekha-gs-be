package com.rekha.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.SecUser;

@Repository
public interface SecUserRepository extends JpaRepository<SecUser, Long>, JpaSpecificationExecutor<SecUser>{

	SecUser findByPhoneNumber(String username);

	SecUser findByPhoneNumberAndIsActive(String phoneNumber, boolean b);

}
