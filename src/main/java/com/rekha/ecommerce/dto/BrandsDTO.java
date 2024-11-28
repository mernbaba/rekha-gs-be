package com.rekha.ecommerce.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BrandsDTO {

	private Long brandId;

	private String brandName;

	private String categoryName;
	
	private Boolean needImage;
	
	private byte[] brandImage;
	
	private Boolean isActive;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
