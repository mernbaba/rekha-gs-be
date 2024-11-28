package com.rekha.ecommerce.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FieldOfficerDetailsDTO {

	private Long id;

	private String fieldOfficerName;

	private String phoneNumber;

	private String dob;

	private String email;

	private String qualification;
	
	private Boolean isActive;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
