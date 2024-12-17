package com.rekha.ecommerce.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.FileStorage;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.FileStorageRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@Slf4j
public class DigitalOceanSpacesService {

	@Autowired
	private S3Client s3Client;

	@Autowired
	FileStorageRepository fileStorageRepository;

	@Value("${do.bucket.name}")
	private String spaceName;

	@Value("${do.bucket.endpoint}")
	private String endpoint;

	@Value("${digital.file.dir.name}")
	private String fileDirName;
	
	
	public List<Map<String, Object>> fetchDoFiles(List<Long> fileIds) throws IOException {
	    if (fileIds == null || fileIds.isEmpty()) {
	        throw new CloudBaseException(ResponseCode.UNKNOWN_ERROR_OCCURRED);
	    }

	    // Fetching a list of FileStorage entities matching the provided file IDs
	    List<FileStorage> fileEntities = fileStorageRepository.findByFileStorageIdIn(fileIds);

	    if (fileEntities.isEmpty()) {
	        throw new CloudBaseException(ResponseCode.DATA_NOT_FOUND);
	    }

	    List<Map<String, Object>> responses = new ArrayList<>();

	    for (FileStorage entity : fileEntities) {
	        String key = fileDirName + "/" + entity.getFileType() + "/" + entity.getFileName();

	        // Build the S3 GetObjectRequest for the current file
	        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
	                .bucket(spaceName)
	                .key(key)
	                .build();

	        // Retrieve the file as a stream and return it as a byte array
	        try (ResponseInputStream<GetObjectResponse> s3ObjectStream = s3Client.getObject(getObjectRequest)) {
	            byte[] image = s3ObjectStream.readAllBytes();

	            // Prepare a single response map for the file
	            Map<String, Object> response = new HashMap<>();
	            response.put("imageId", entity.getFileStorageId());
	            response.put("fileName", entity.getActualFileName());
	            response.put("image", image);
	            response.put("extension", entity.getActualFileName()
	                    .substring(entity.getActualFileName().lastIndexOf('.') + 1));

	            responses.add(response); // Add the response to the list
	        } catch (IOException e) {
	            throw new CloudBaseException(ResponseCode.DATA_NOT_FOUND);
	        }
	    }

	    return responses;
	}

	

	@Transactional
	public ResponseObject<?> uploadImage(String fileType, MultipartFile[] files, String user) {
		try {

        log.info("file type : " + fileType);

			List<Map<String, Object>> listofMaps = new ArrayList<>();

			for (MultipartFile file : files) {

				fileType = fileType.toUpperCase().trim().replace(" ", "_");

				// Generate a new UUID
				String originalFileName = file.getOriginalFilename();
				String extension = "";
				if (originalFileName != null) {
					extension = FilenameUtils.getExtension(originalFileName);
				}

				// Combine UUID with the file extension, keeping the dot before extension
				String fileName = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);

				String key = fileDirName + "/" + fileType + "/" + fileName; // Folder and file name

				try (InputStream inputStream = file.getInputStream()) {

					PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(spaceName).key(key)
							.acl(ObjectCannedACL.PRIVATE).contentType(file.getContentType())
							.contentDisposition("inline").build();

					PutObjectResponse response = s3Client.putObject(putObjectRequest,
							RequestBody.fromInputStream(inputStream, file.getSize()));

					if (response.sdkHttpResponse().isSuccessful()) {
//                log.error("Response object :: " + response);
//				String encodedObjectKey = URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
//				encodedObjectKey = encodedObjectKey.replace("+", "%20");

						String url = endpoint + "/" + key;
//                log.error("url " + url);
						FileStorage storage = new FileStorage();
//                storage.setCompanyCode(company);
//                storage.setBranchCode(branch);
						storage.setBucketName(spaceName);
						storage.setFolderName(fileDirName);
//                storage.setSubFolderName(branch);
						storage.setFileType(fileType);
						storage.setFileName(fileName);
						storage.setActualFileName(originalFileName);
						storage.setFileEndpoint(url);
						storage.setCreatedBy(user);
						storage.setIsPublicAccess(false);
						storage.setIsEncrypted(false);
						storage.setIsDiscarded(false);
						storage.setIsTtl(false);
						storage.setIsActive(true);
						Long id = fileStorageRepository.save(storage).getFileStorageId();
						Map<String, Object> map = new HashMap<>();
						map.put("fileId", id);
						map.put("fileName", fileName);
						map.put("fileType", fileType);
						listofMaps.add(map);
						
					} else {
						throw new RuntimeException("Failed to upload file to DigitalOcean Spaces");
					}
				}
			}
			return ResponseObject.success(listofMaps);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}
	
	@Transactional
	public ResponseObject<Object> deleteFileFromDO(Long fileId) {
	    // Retrieve file entity from database or throw exception if not found
	    FileStorage fileStorage = fileStorageRepository.findById(fileId)
	            .orElseThrow(() -> new CloudBaseException(ResponseCode.BAD_REQUEST));

	    // Build the S3 key using fileStorage properties
	    String key = fileDirName + "/" + fileStorage.getFileType() + "/" + fileStorage.getFileName();

	    try {
	        // Check if the file exists in S3
	        if (!doesObjectExist(spaceName, key)) {
	            return new ResponseObject<>("File does not exist: " + key, ResponseCode.FAILED);
	        }

	        // Create the S3 delete request and delete the object
	        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
	                .bucket(spaceName)
	                .key(key)
	                .build();

	        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest);
	        log.info("File deleted successfully: {}", deleteObjectResponse);

	        // Update the database to mark the file as inactive
	        fileStorageRepository.updateIsActiveByfileId(fileId);

	        return new ResponseObject<>("File Deleted: " + fileId, ResponseCode.SUCCESS);

	    } catch (S3Exception e) {
	        // Handle S3-specific errors
	        log.error("Failed to delete file: {}", e.getMessage());
	        return new ResponseObject<>("Failed to delete file: " + e.getMessage(), ResponseCode.FAILED);
	    } catch (Exception e) {
	        // Handle any other unexpected errors
	        log.error("An unexpected error occurred: {}", e.getMessage());
	        return new ResponseObject<>("An unexpected error occurred", ResponseCode.FAILED);
	    }
	}
	
	 public boolean doesObjectExist(String bucketName, String key) {
	        try {
	            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder().bucket(bucketName).key(key).build();

	            s3Client.headObject(headObjectRequest);
	            return true;
	        } catch (NoSuchKeyException e) {
	            if (!(e instanceof NoSuchKeyException)) {
	                e.printStackTrace();
	            }
	            log.error("Object does not exist: " + key);

	            return false;
	        } catch (S3Exception e) {
	            log.error("Error occurred while checking object existence: " + e.awsErrorDetails().errorMessage());
	            return false;
	        }
	    }

//	    @Transactional
//	    public Boolean deleteFileById(Long fileId) {
//	        String key = null;
//	        DeleteObjectResponse deleteObjectResponse = null;
//	        Optional<FileStorage> optEntity = fileStorageRepository.findById(fileId);
//	        if (optEntity.isPresent()) {
//	            key = fileDirName + "/" + optEntity.get().getFileType() + "/" + optEntity.get().getFileName();
//	        } else {
//	            return false;
//	        }
//	        try {
//	            if (!doesObjectExist(spaceName, key)) {
//	                return false;
//	            }
//	            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(spaceName).key(key).build();
//
//	            deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest);
//	            log.error("File deleted successfully" + deleteObjectResponse);
//
//	            fileStorageRepository.updateIsActiveByfileId(fileId);
//	            return true;
//	        } catch (S3Exception e) {
//	            if (!(e instanceof NoSuchKeyException)) {
//	                log.error("Failed to delete file: " + e.getMessage());
//	            }
//	            return false;
//	        }
//	    }
	
}
