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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_brands", uniqueConstraints = @UniqueConstraint(name = "uk_category_brand", columnNames = {
		"brand_name", "category_name" }))
public class Brands {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "brand_id")
	private Long brandId;

	@Column(name = "brand_name", nullable = false, length = 56)
	private String brandName;

	@Column(name = "category_name", nullable = false, length = 56)
	private String categoryName;

//	@Lob
//	@Column(name = "brand_image")
//	private Blob brandImage;

	@Column(name = "brand_image_id")
	private Long brandImageId;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

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
	@JoinColumn(name = "category_name", referencedColumnName = "category_name", insertable = false, updatable = false)
	private Category category;

	@OneToOne
	@JoinColumn(name = "brand_image_id", referencedColumnName = "file_storage_id", insertable = false, updatable = false)
	private FileStorage fileStorage;

	@PrePersist
	protected void onCreate() {
		if (isActive == null) {
			isActive = true;
		}
	}
}
