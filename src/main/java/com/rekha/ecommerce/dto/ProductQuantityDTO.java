package com.rekha.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductQuantityDTO {

	private Long id;

	private Long productId;

	private String quantity;

	private BigDecimal amount;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
