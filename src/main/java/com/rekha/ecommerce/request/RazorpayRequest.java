package com.rekha.ecommerce.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RazorpayRequest {
	
	private String orderId;
	private String paymentId;
	private String razorSecKey;
	
	private BigDecimal amount;

//	private String currency;

}
