package com.rekha.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.BrandsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.Brands;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.BrandsRepository;

import jakarta.transaction.Transactional;

@Service
public class BrandsService {

	@Autowired
	BrandsRepository brandsRepository;

	public ResponseObject<List<BrandsDTO>> getBrandsByCategory(String category) {

		List<BrandsDTO> dtoList = new ArrayList<BrandsDTO>();
		List<Brands> brands = new ArrayList<>();
		if (category != null && category.equalsIgnoreCase("All")) {
			brands = brandsRepository.findAll();
		} else {
			brands = brandsRepository.findByCategoryName(category);
		}
		brands.forEach(entity -> {
			BrandsDTO dto = new BrandsDTO();
			BeanUtils.copyProperties(entity, dto);
			/*Blob brandImageBlob = entity.getBrandImage(); // Assuming getBrandImage() returns a Blob
			if (brandImageBlob != null) {
				try {
					byte[] brandImageBytes = brandImageBlob.getBytes(1, (int) brandImageBlob.length()); // Convert Blob
																										// to byte[]
					dto.setBrandImage(brandImageBytes); // Set the byte[] in DTO
				} catch (SQLException e) {
					// Handle the error if Blob conversion fails
					e.printStackTrace();
				}
			}*/
			dtoList.add(dto);
		});

		return ResponseObject.success(dtoList);
	}

	@Transactional
	public ResponseObject<BrandsDTO> save(BrandsDTO dto, String username) {

		try {
			Brands entity = brandsRepository.findByBrandNameAndCategoryName(dto.getBrandName(), dto.getCategoryName());
			if (entity != null) {
				throw new CloudBaseException(ResponseCode.DUPLICATE_ENTRY);
			}
			entity = new Brands();
			BeanUtils.copyProperties(dto, entity);

			/*byte[] brandImageBytes = dto.getBrandImage();
			Blob brandImageBlob = new SerialBlob(brandImageBytes);
			entity.setBrandImage(brandImageBlob);*/
			entity.setCreatedBy(username);

			brandsRepository.save(entity);

			BrandsDTO response = new BrandsDTO();
			BeanUtils.copyProperties(entity, response);

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
	public ResponseObject<BrandsDTO> update(BrandsDTO dto, String username) {
		try {
			Brands entity = brandsRepository.findById(dto.getBrandId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.BRAND_NOT_FOUND));
			BeanUtils.copyProperties(dto, entity);

			entity.setLastModifiedBy(username);

			brandsRepository.save(entity);

			BrandsDTO response = new BrandsDTO();
			BeanUtils.copyProperties(entity, response);

			return ResponseObject.success(response);
		} catch (CloudBaseException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	public ResponseObject<List<BrandsDTO>> getAllBrands() {
		List<BrandsDTO> brandsList = new ArrayList<>();
		brandsRepository.findAll().forEach(brand -> {
			BrandsDTO dto = new BrandsDTO();
			BeanUtils.copyProperties(brand, dto);
			/*if (brand.getBrandImage() != null) {
				try {
					byte[] brandImageBytes = brand.getBrandImage().getBytes(1, (int) brand.getBrandImage().length());
					dto.setBrandImage(brandImageBytes);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}*/
			brandsList.add(dto);
		});
		return ResponseObject.success(brandsList);
	}

}
