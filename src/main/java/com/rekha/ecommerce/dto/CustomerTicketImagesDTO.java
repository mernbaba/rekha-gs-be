package com.rekha.ecommerce.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CustomerTicketImagesDTO {

	private Long id;

	private Long ticketId;

//	private byte[] image;
	
	private Long imageId;

	private String createdBy;

	private LocalDateTime createdDate;

}
