package com.rekha.ecommerce.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductImagesDTO {

	private Long id;

	private Long productId;

//	private byte[] productImage;

	private Long productImageId;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
