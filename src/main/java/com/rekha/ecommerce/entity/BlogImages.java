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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_blog_images")
public class BlogImages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "blog_id", nullable = false)
	private Long blogId;

//	@Lob
//	@Column(name = "blob_image", nullable = false)
//	private Blob blogImage;

	@Column(name = "blog_image_id")
	private Long blogImageId;

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
	@JoinColumn(name = "blog_id", referencedColumnName = "id", insertable = false, updatable = false)
	Blog blog;

	@OneToOne
	@JoinColumn(name = "blog_image_id", referencedColumnName = "file_storage_id", insertable = false, updatable = false)
	private FileStorage fileStorage;

}
