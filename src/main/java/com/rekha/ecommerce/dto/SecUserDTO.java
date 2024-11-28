package com.rekha.ecommerce.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SecUserDTO {

	private Long id;

	private String phoneNumber;
	
	private String password;

	private String userName;

	private String userCode;

	private Boolean isAdmin;
	
	private Boolean isActive;

	private Boolean isFieldOfficer;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
