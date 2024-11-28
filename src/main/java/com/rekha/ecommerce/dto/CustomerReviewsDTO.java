package com.rekha.ecommerce.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CustomerReviewsDTO {

	private Long id;

	private String customerName;

	private String phoneNumber;

	private Integer rating;

	private String review;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
