package com.rekha.ecommerce.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_file_storage")
public class FileStorage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_storage_id")
	private Long fileStorageId;

	/*@Column(name = "company_code", nullable = false)
	private String companyCode;
	
	@Column(name = "branch_code", nullable = false)
	private String branchCode;*/

	@Column(name = "bucket_name", nullable = false, length = 56)
	private String bucketName;

	@Column(name = "folder_name", nullable = false)
	private String folderName;

	/*@Column(name = "sub_folder_name")
	private String subFolderName;*/

	@Column(name = "file_type", nullable=false, length = 56)
	private String fileType;

	@Column(name = "file_name", nullable = false, length = 100)
	private String fileName;

	@Column(name = "file_endpoint", nullable = false)
	private String fileEndpoint;

	@Column(name = "is_public_access", nullable = false)
	private Boolean isPublicAccess;

	@Column(name = "is_encrypted", nullable = false)
	private Boolean isEncrypted;

	@Column(name = "is_discarded", nullable = false)
	private Boolean isDiscarded;

	@Column(name = "is_ttl", nullable = false)
	private Boolean isTtl;

	@Column(name = "ttl_value")
	private String ttlValue;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Column(name = "comments")
	private String comments;

	@Column(name = "actual_file_name", nullable = false)
	private String actualFileName;

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

}
