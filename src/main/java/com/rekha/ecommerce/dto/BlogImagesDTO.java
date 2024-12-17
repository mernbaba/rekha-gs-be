package com.rekha.ecommerce.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BlogImagesDTO {

	private Long id;

	private Long blogId;

//	private byte[] blogImage;

	private Long blogImageId;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
