package com.rekha.ecommerce.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FarmerDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532807610197921660L;

	private Long id;

	private String farmerName;

	private String phoneNumber;

	private String villageName;

	private String manualVillageName;

	private Boolean isActive;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
