package com.rekha.ecommerce.entity;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_blog")
public class Blog implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "author_name", nullable = false, length = 56)
	private String authorName;

	@Lob
	@Column(name = "author_avatar")
	private Blob authorAvatar;

	@Column(name = "author_role", nullable = false, length = 56)
	private String authorRole;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@Column(name = "is_active")
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

	@PrePersist
	protected void OnCreate() {
		if (isActive == null) {
			isActive = true;
		}
	}
}
