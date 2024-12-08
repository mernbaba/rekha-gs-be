package com.rekha.ecommerce.entity;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_order_items")
public class OrderItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "order_id", nullable = false)
	private Long orderId;

	@Column(name = "item", nullable = false, length = 100)
	private String item;
	
	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "brand_name", nullable = false, length = 56)
	private String brandName;

	@Lob
	@Column(name = "item_image")
	private Blob itemImage;

	@Column(name = "weight", nullable = false, length = 100)
	private String weight;

	@Column(name = "rate", nullable = false)
	private BigDecimal rate;

	@Column(name = "qty", nullable = false)
	private Integer qty;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

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

	@ManyToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Order order;
}
