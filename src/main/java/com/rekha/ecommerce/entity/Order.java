package com.rekha.ecommerce.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "order_id", nullable = false, unique = true)
	private String orderId;

	@Column(name = "order_date", nullable = false)
	@CreationTimestamp
	private LocalDateTime orderDate;

	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber;

	@Column(name = "customer_name", nullable = false, length = 100)
	private String customerName;

	@Column(name = "total_quantity", nullable = false)
	private Integer totalQuantity;

	@Column(name = "total_amt", nullable = false)
	private BigDecimal totalAmt;

	@Column(name = "discount_code")
	private String discountCode;

	@Column(name = "discount_pct")
	private BigDecimal discountPct;

	@Column(name = "discount_amt")
	private BigDecimal discountAmt;

	@Column(name = "net_total", nullable = false)
	private BigDecimal netTotal;

	@Column(name = "payment_method", nullable = false, length = 56)
	private String paymentMethod;

	@Column(name = "order_status", nullable = false, length = 56)
	private String orderStatus;

	@Column(name = "delivery_date")
	private LocalDate deliveryDate;

	@Column(name = "delivery_address", nullable = false, columnDefinition = "TEXT")
	private String deliveryAddress;

	@Column(name = "field_officer_name", length = 56)
	private String fieldOfficerName;

	@Column(name = "created_by", nullable = false, length = 32)
	private String createdBy;

	@Column(name = "created_date", nullable = false)
	@CreationTimestamp
	private LocalDateTime createdDate;

	@Column(name = "last_modified_by", length = 32)
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	@UpdateTimestamp
	private LocalDateTime lastModifiedDate;

}
