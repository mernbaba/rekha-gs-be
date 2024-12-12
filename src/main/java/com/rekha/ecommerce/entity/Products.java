package com.rekha.ecommerce.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_products", uniqueConstraints = @UniqueConstraint(name = "uk_product_brnd_cat", columnNames = {
		"product_name", "brand_name", "category_name" }))
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long productId;

	@Column(name = "product_name", nullable = false, length = 56)
	private String productName;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "rating")
	private Integer rating;
	
	@Column(name="priority" ,nullable=false)
	private Boolean priority;

	@Column(name = "technical_name", nullable = false, length = 100)
	private String technicalName;

	@Column(name = "brand_name", nullable = false, length = 56)
	private String brandName;

	@Column(name = "category_name", nullable = false, length = 56)
	private String categoryName;

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
	@JoinColumn(name = "brand_name", referencedColumnName = "brand_name", insertable = false, updatable = false)
	private Brands brands;

	@ManyToOne
	@JoinColumn(name = "category_name", referencedColumnName = "category_name", insertable = false, updatable = false)
	private Category category;

	@PrePersist
	protected void onCreate() {
		if (isActive == null) {
			isActive = true;
		}
		if (priority==null) {
			priority=false;
		}
	}

}
