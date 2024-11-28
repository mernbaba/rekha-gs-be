package com.rekha.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.dto.UserAddressDetailsDTO;
import com.rekha.ecommerce.entity.UserAddressDetails;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.UserAddressDetailsRepository;

@Service
public class UserAddressDetailsService {

	@Autowired
	private UserAddressDetailsRepository addressDetailsRepository;

	public ResponseObject<UserAddressDetailsDTO> save(UserAddressDetailsDTO detailsDTO, String phoneNumber,
			String userName) {
		try {

			UserAddressDetails details = new UserAddressDetails();
			detailsDTO.setPhoneNumber(phoneNumber);
			detailsDTO.setCreatedBy(userName);
			BeanUtils.copyProperties(detailsDTO, details);
			addressDetailsRepository.save(details);

			UserAddressDetailsDTO response = new UserAddressDetailsDTO();
			BeanUtils.copyProperties(details, response);
			return ResponseObject.success(response);

		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	public ResponseObject<UserAddressDetailsDTO> update(UserAddressDetailsDTO detailsDTO, String userName,
			String phoneNumber) {

		try {
			addressDetailsRepository.findById(detailsDTO.getId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.DETAILS_NOT_FOUND));
			UserAddressDetails details = new UserAddressDetails();
			detailsDTO.setPhoneNumber(phoneNumber);
			detailsDTO.setLastModifiedBy(userName);
			BeanUtils.copyProperties(detailsDTO, details);
			addressDetailsRepository.save(details);

			UserAddressDetailsDTO response = new UserAddressDetailsDTO();
			BeanUtils.copyProperties(details, response);
			return ResponseObject.success(response);

		} catch (CloudBaseException exp) {
			exp.printStackTrace();
			throw exp;
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	public ResponseObject<?> getUserAddress(String phoneNumber) {
		try {

			List<UserAddressDetailsDTO> responseList = new ArrayList<>();
			addressDetailsRepository.findByPhoneNumber(phoneNumber).forEach(address -> {
				UserAddressDetailsDTO dto = new UserAddressDetailsDTO();
				BeanUtils.copyProperties(address, dto);
				responseList.add(dto);
			});
			return ResponseObject.success(responseList);
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.DATA_NOT_FOUND);
		}
	}

}
