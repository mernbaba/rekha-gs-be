package com.rekha.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderItemsDTO {

	private Long id;
	private Long orderId;
	private String item;
	private String brandName;
	private byte[] itemImage;
	private String weight;
	private BigDecimal rate;
	private Integer qty;
	private BigDecimal amount;
	private String createdBy;
	private LocalDateTime createdDate;
	private String lastModifiedBy;
	private LocalDateTime lastModifiedDate;

}
