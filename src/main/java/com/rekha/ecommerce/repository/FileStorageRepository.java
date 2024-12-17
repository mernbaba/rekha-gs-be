package com.rekha.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.rekha.ecommerce.entity.FileStorage;

public interface FileStorageRepository extends JpaRepository<FileStorage, Long>, JpaSpecificationExecutor<FileStorage> {

	@Modifying
	@Query(value = "update tb_file_storage h set h.is_active=false where h.file_storage_id=:fileId", nativeQuery = true)
	void updateIsActiveByfileId(Long fileId);

	@Query(value = "select file_storage_id from tb_file_storage where file_name=:fileName", nativeQuery = true)
	Long getFileIdByFileName(String fileName);

	List<FileStorage> findByFileStorageIdIn(List<Long> fileIds);
	
	
}
