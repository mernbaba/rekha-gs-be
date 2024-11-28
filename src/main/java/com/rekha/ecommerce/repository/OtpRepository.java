//package com.rekha.ecommerce.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.rekha.ecommerce.entity.Otp;
//
//import jakarta.transaction.Transactional;
//
//@Repository
//public interface OtpRepository extends JpaRepository<Otp, Long>, JpaSpecificationExecutor<Otp> {
//
//	@Query(value = "select * from tb_otp where phone_number=:phoneNumber ORDER BY createddate DESC LIMIT 1", nativeQuery = true)
//	Otp findByPhoneNumberforOtp(String phoneNumber);
//
//	@Query(value = "update tb_otp set otp_status=:status where id=:id ", nativeQuery = true)
//	@Transactional
//	@Modifying
//	void updateOtpStatusByIdAndPartyCode(String status, Long id);
//
//}
