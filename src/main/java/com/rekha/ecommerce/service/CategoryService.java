package com.rekha.ecommerce.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.CategoryDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.Category;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional
	public ResponseObject<CategoryDTO> save(CategoryDTO categoryDTO, String userName) {
		try {

			Category categoryEntity = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
			if (categoryEntity != null && ObjectUtils.isNotEmpty(categoryEntity)) {
				throw new CloudBaseException(ResponseCode.DUPLICATE_ENTRY);
			}
			categoryEntity = new Category();
			BeanUtils.copyProperties(categoryDTO, categoryEntity);
			
			// Blob convertion
			byte[] cateImageBytes = categoryDTO.getCategoryImage();
			Blob cateImageBlob = new SerialBlob(cateImageBytes);
			
			categoryEntity.setCreatedBy(userName);
			categoryEntity.setCategoryImage(cateImageBlob);
			
			categoryRepository.save(categoryEntity);
			CategoryDTO response = new CategoryDTO();
			BeanUtils.copyProperties(categoryEntity, response);
			
			Blob catImageBlob = categoryEntity.getCategoryImage(); // Assuming getBrandImage() returns a Blob
			if (catImageBlob != null) {
				try {
					byte[] catImageBytes = catImageBlob.getBytes(1, (int) catImageBlob.length()); // Convert Blob
																										// to byte[]
					response.setCategoryImage(catImageBytes); // Set the byte[] in DTO
				} catch (SQLException e) {
					// Handle the error if Blob conversion fails
					e.printStackTrace();
				}
			}
			return ResponseObject.success(response);

		} catch (CloudBaseException exp) {
			exp.printStackTrace();
			throw exp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	@Transactional
	public ResponseObject<CategoryDTO> update(CategoryDTO categoryDTO, String userName) {
		try {

			categoryRepository.findById(categoryDTO.getCategoryId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.CATEGORY_NOT_FOUND));
			Category categoryEntity = new Category();
			categoryDTO.setLastModifiedBy(userName);
			BeanUtils.copyProperties(categoryDTO, categoryEntity);
			categoryRepository.save(categoryEntity);
			CategoryDTO response = new CategoryDTO();
			BeanUtils.copyProperties(categoryEntity, response);
			return ResponseObject.success(response);
		} catch (CloudBaseException exp) {
			exp.printStackTrace();
			throw exp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	public ResponseObject<List<CategoryDTO>> getAllCategories() {
		List<CategoryDTO> list = new ArrayList<>();
		categoryRepository.findAll().forEach(category -> {
			CategoryDTO dto = new CategoryDTO();
			BeanUtils.copyProperties(category, dto);
			if(category.getCategoryImage() != null) {
				try {
					byte[] catImageBytes = category.getCategoryImage().getBytes(1, (int) category.getCategoryImage().length()); 
					dto.setCategoryImage(catImageBytes);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			list.add(dto);
		});
		return ResponseObject.success(list);
	}

}
