package com.rekha.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderDTO {

	private Long id;

	private String orderId;

	private LocalDateTime orderDate;

	private String phoneNumber;

	private String customerName;

	private Integer totalQuantity;

	private BigDecimal totalAmt;

	private String discountCode;

	private BigDecimal discountPct;

	private BigDecimal discountAmt;

	private BigDecimal netTotal;

	private String paymentMethod;

	private String orderStatus;

	private LocalDate deliveryDate;

	private String deliveryAddress;

	private String fieldOfficerName;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
