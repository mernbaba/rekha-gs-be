package com.rekha.ecommerce.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_customer_ticket_images")
public class CustomerTicketImages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "ticket_id")
	private Long ticketId;

//	@Lob
//	@Column(name = "image")
//	private Blob image;

	@Column(name = "image_id")
	private Long imageId;

	@Column(name = "created_by", nullable = false, length = 32)
	private String createdBy;

	@Column(name = "created_date", nullable = false)
	@CreationTimestamp
	private LocalDateTime createdDate;

	@ManyToOne
	@JoinColumn(name = "ticket_id", referencedColumnName = "id", insertable = false, updatable = false)
	private CustomerTickets customerTickets;

	@OneToOne
	@JoinColumn(name = "image_id", referencedColumnName = "file_storage_id", insertable = false, updatable = false)
	private FileStorage fileStorage;

}
