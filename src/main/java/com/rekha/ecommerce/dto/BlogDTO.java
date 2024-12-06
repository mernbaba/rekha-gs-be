package com.rekha.ecommerce.dto;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BlogDTO {

	private Long id;

	private List<BlogImagesDTO> images;

	private String description;

	private Boolean isActive;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
