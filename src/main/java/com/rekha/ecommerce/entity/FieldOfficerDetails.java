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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_field_officer_details")
public class FieldOfficerDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "field_officer_name", nullable = false, length = 56)
	private String fieldOfficerName;

	@Column(name = "phone_number", unique = true, nullable = false, length = 20)
	private String phoneNumber;

	@Column(name = "dob", nullable = false, length = 10)
	private String dob;

	@Column(name = "email", unique = true, nullable = false, length = 20)
	private String email;

	@Column(name = "qualification")
	private String qualification;

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

//	@OneToOne
//	@JoinColumn(name = "phone_number", referencedColumnName = "phone_number", insertable = false, updatable = false)
//	private SecUser secUser;
	
	@PrePersist
	protected void onCreate() {
		if (isActive == null) {
			isActive = true;
		}
	}

}
