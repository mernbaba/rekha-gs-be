package com.rekha.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ProductsDTO {

	private Long productId;

	private String productName;

	private String technicalName;

	private String description;

	private Integer rating;

	private Boolean priority;

//	private List<byte[]> productImages;
	private List<Long> productImageIds;

	private Boolean isActive;

	private String brandName;

	private String categoryName;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

	private List<ProductImagesDTO> imagesDTOs;

	private List<ProductQuantityDTO> productQuantityDTOList;
}
