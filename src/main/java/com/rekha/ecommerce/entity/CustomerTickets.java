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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "tb_customer_tickets")
@Entity
public class CustomerTickets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -481855733515560337L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id")
	private Long ticketId;

	@Column(name = "date", nullable = false)
	@CreationTimestamp
	private LocalDate date;

	@Lob
	@Column(name = "crop_image")
	private Blob cropImage;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber;

	@Column(name = "customer_name", nullable = false, length = 100)
	private String customerName;

	@Column(name = "problem", columnDefinition = "TEXT")
	private String problem;

	@Column(name = "solution", columnDefinition = "TEXT")
	private String solution;

	@Column(name = "type")
	private String type;

	@Column(name = "field_officer_name", nullable = false, length = 56)
	private String fieldOfficerName;

	@Column(name = "status", length = 20)
	private String status;

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
	
//	picture list need to store in other tbl

}
