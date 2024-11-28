package com.rekha.ecommerce.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_payment_gateway")
public class PaymentGateway implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "order_id", nullable = false)
	private String orderId;

	@Column(name = "ref_type")
	private String refType;

	@Column(name = "status")
	private String status;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "currency")
	private String currency;

	@Column(name="receipt_id")
	private String receiptId;

	@Column(name = "payment_id")
	private String paymentId;

	@Column(name = "attempts")
	private Integer attempts;

	@Column(name = "notes")
	private String notes;

//	@Column(name = "gateway_signature")
//	private String gatewaySignature;

	@Column(name = "gateway")
	private String gateway;

	@Column(name = "gateway_created_date")
	private Date gatewayCreatedDate;

//	@Column(name="company_code")
//	private String companyCode;

	@Column(name = "createdby")
	private String createdby;

	@Column(name = "createddate", nullable = false)
	private Date createddate;

}
