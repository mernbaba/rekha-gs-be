package com.rekha.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.SecUser;

import jakarta.transaction.Transactional;

@Repository
public interface SecUserRepository extends JpaRepository<SecUser, Long>, JpaSpecificationExecutor<SecUser>{

	SecUser findByPhoneNumber(String username);

	SecUser findByPhoneNumberAndIsActive(String phoneNumber, boolean b);
	
	@Modifying
	@Transactional
	@Query(value = "update tb_sec_user set password=:newPwd , last_modified_by=:userName where phone_number=:phoneNumber ", nativeQuery = true)
	void updatePassword(String phoneNumber, String newPwd, String userName);

}
