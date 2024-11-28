package com.rekha.ecommerce.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserAddressDetailsDTO {

	private Long id;

	private String phoneNumber;
	
	private String userPhoneNumber;

	private String address;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
