//package com.rekha.ecommerce.entity;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Data
//@Table(name = "tb_otp")
//@Entity
//public class Otp implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id", nullable = false)
//	private Long id;
//
//	@Column(name = "otp", nullable = false)
//	private String otp;
//
//	@Column(name = "phone_number", nullable = false, length = 20)
//	private String phoneNumber;
//
//	@Column(name = "otp_gentime", nullable = false)
//	private LocalDateTime otpGentime;
//
//	@Column(name = "otp_exptime", nullable = false)
//	private LocalDateTime otpExptime;
//
//	@Column(name = "otp_status")
//	private String otpStatus;
//
//	@Column(name = "createdby", nullable = false)
//	private String createdby;
//
//	@Column(name = "createddate", nullable = false)
//	private LocalDateTime createddate;
//
//	@Column(name = "lastmodifiedby")
//	private String lastmodifiedby;
//
//	@Column(name = "lastmodifieddate")
//	private LocalDateTime lastmodifieddate;
//}
