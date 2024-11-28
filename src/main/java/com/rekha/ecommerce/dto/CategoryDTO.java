package com.rekha.ecommerce.dto;

import java.sql.Blob;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CategoryDTO {

	private Long categoryId;

	private String categoryName;
	
	private Boolean needImage;
	
	private byte[] categoryImage;
	
	private Boolean isActive;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
