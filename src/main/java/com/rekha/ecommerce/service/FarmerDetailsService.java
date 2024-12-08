package com.rekha.ecommerce.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.FarmerDetailsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.FarmerDetails;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.FarmerDetailsRepository;
import com.rekha.ecommerce.utils.ObjectMapperUtil;

@Service
public class FarmerDetailsService {

	@Autowired
	private FarmerDetailsRepository repository;

	public ResponseObject<FarmerDetailsDTO> saveFarmer(FarmerDetailsDTO farmerDetailsDTO, String username) {
		try {

			FarmerDetails farmerDetails = ObjectMapperUtil.convertEntityToDTO(farmerDetailsDTO, FarmerDetails.class);

			farmerDetails.setCreatedBy(username);

			farmerDetails = repository.save(farmerDetails);
			return ResponseObject.success(ObjectMapperUtil.convertEntityToDTO(farmerDetails, FarmerDetailsDTO.class));
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	public ResponseObject<FarmerDetailsDTO> updateFarmer(FarmerDetailsDTO farmerDetailsDTO, String username) {
		try {

			repository.findById(farmerDetailsDTO.getId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.NOT_FOUND));

			FarmerDetails farmerDetails = ObjectMapperUtil.convertEntityToDTO(farmerDetailsDTO, FarmerDetails.class);

			farmerDetails.setLastModifiedBy(username);

			farmerDetails = repository.save(farmerDetails);
			return ResponseObject.success(ObjectMapperUtil.convertEntityToDTO(farmerDetails, FarmerDetailsDTO.class));
		} catch (CloudBaseException exp) {
			exp.printStackTrace();
			throw exp;
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	public ResponseObject<?> getAll() {

		List<FarmerDetailsDTO> response = ObjectMapperUtil.convertEntityListToDTOList(repository.findAll(),
				FarmerDetailsDTO.class);

		return ResponseObject.success(response);
	}

}
