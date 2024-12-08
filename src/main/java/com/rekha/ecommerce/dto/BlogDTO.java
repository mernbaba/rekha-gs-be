package com.rekha.ecommerce.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BlogDTO {

	private Long id;

	private List<BlogImagesDTO> images;

	private String authorName;

	private byte[] authorAvatar;

	private String authorRole;

	private LocalDate date;

	private String content;

	private Boolean isActive;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

}
