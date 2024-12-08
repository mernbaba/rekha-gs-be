package com.rekha.ecommerce.entity;

import java.io.Serializable;
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
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_customer_reviews", uniqueConstraints = @UniqueConstraint(name = "uk_review", columnNames = {
		"product_id", "phone_number" }))
public class CustomerReviews implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "customer_name", nullable = false, length = 100)
	private String customerName;
	
	@Lob
	@Column(name = "customer_avatar")
	private Blob customerAvatar;

	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber;

	@Column(name = "rating")
	private Integer rating;

	@Column(name = "review", columnDefinition = "TEXT")
	private String review;

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
	@JoinColumn(name = "phone_number", referencedColumnName = "phone_number", insertable = false, updatable = false)
	private SecUser secUser;
}
