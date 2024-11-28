package com.rekha.ecommerce.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CustomerTicketsDTO {

	private Long ticketId;

	private LocalDate date;

	private byte[] cropImage;

	private String phoneNumber;

	private String customerName;

	private String solution;

	private String problem;

	private String fieldOfficerName;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

//	picture list need to store in other tbl

}
