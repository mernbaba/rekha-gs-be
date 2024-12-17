package com.rekha.ecommerce.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_product_images")
public class ProductImages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_id", nullable = false)
	private Long productId;

//	@Lob
//	@Column(name = "product_image")
//	private Blob productImage;

	@Column(name = "product_image_id")
	private Long productImageId;

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
	@JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false)
	Products products;

	@OneToOne
	@JoinColumn(name = "product_image_id", referencedColumnName = "file_storage_id", insertable = false, updatable = false)
	private FileStorage fileStorage;

}
