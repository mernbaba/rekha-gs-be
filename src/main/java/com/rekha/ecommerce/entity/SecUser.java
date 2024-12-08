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
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_sec_user")
public class SecUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "phone_number", unique = true, nullable = false, length = 20)
	private String phoneNumber;

	@Column(name = "user_name", nullable = false, length = 32)
	private String userName;

	@Column(name = "password", nullable = false)
	private String password;
	
	@Lob
	@Column(name = "profile")
	private Blob profile;

	@Column(name = "user_code", unique = true, nullable = false, length = 56)
	private String userCode;

	@Column(name = "is_admin")
	private Boolean isAdmin;

	@Column(name = "is_field_officer")
	private Boolean isFieldOfficer;

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

	@PrePersist
	protected void onCreate() {
		if (isAdmin == null) {
			isAdmin = false;
		}
		if (isFieldOfficer == null) {
			isFieldOfficer = false;
		}
		if (isActive == null) {
			isActive = true;
		}
	}

}
