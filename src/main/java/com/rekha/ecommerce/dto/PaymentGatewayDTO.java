package com.rekha.ecommerce.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class PaymentGatewayDTO implements Serializable{


	private static final long serialVersionUID = 1L;

	private Long id;

	private String orderId;

	private String refType;

	private String status;

	private BigDecimal amount;

	private String currency;

	private String paymentId;

	private Integer attempts;

	private String notes;

//	private String gatewaySignature;

	private String gateway;

	private LocalDateTime gatewayCreatedDate;

	private String createdby;

	private LocalDateTime createddate;

}
