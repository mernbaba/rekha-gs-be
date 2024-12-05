package com.rekha.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.CustomerReviewsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.CustomerReviews;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.CustomerReviewsRepository;
import com.rekha.ecommerce.utils.ObjectMapperUtil;

@Service
public class CustomerReviewsService {

	@Autowired
	CustomerReviewsRepository customerReviewsRepository;

	public ResponseObject<?> getAllReviewByProductId(Long id) {

		List<CustomerReviews> customerReviewsList = customerReviewsRepository.findByProductId(id);
		List<CustomerReviewsDTO> dtoList = new ArrayList<>();

		customerReviewsList.forEach(entity -> {
			CustomerReviewsDTO dto = new CustomerReviewsDTO();
			BeanUtils.copyProperties(entity, dto);
			dtoList.add(dto);
		});
		return ResponseObject.success(dtoList);
	}

	public ResponseObject<?> save(CustomerReviewsDTO dto, String phoneNumber, String userName) {

		try {

			CustomerReviews customerEntity = ObjectMapperUtil.convertDTOToEntity(dto, CustomerReviews.class);

			
			customerEntity.setCustomerName(userName);
			customerEntity.setPhoneNumber(phoneNumber);
			customerEntity.setCreatedBy(userName);
			
			customerReviewsRepository.save(customerEntity);

			CustomerReviewsDTO responseDTO = ObjectMapperUtil.convertEntityToDTO(customerEntity,
					CustomerReviewsDTO.class);
			return ResponseObject.success(responseDTO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	public ResponseObject<?> update(CustomerReviewsDTO dto, String userName) {

		try {
			CustomerReviews customerEntity = customerReviewsRepository.findById(dto.getId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.NOT_FOUND));

			customerEntity = ObjectMapperUtil.convertDTOToEntity(customerEntity, CustomerReviews.class);

			customerEntity.setLastModifiedBy(userName);

			customerReviewsRepository.save(customerEntity);

			CustomerReviewsDTO responseDTO = ObjectMapperUtil.convertEntityToDTO(customerEntity,
					CustomerReviewsDTO.class);
			return ResponseObject.success(responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}
}
