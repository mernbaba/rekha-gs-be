package com.rekha.ecommerce.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.CustomerReviewsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.CustomerReviews;
import com.rekha.ecommerce.entity.SecUser;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.CustomerReviewsRepository;
import com.rekha.ecommerce.repository.SecUserRepository;
import com.rekha.ecommerce.utils.ImageConversion;
import com.rekha.ecommerce.utils.ObjectMapperUtil;

import jakarta.transaction.Transactional;

@Service
public class CustomerReviewsService {

	@Autowired
	CustomerReviewsRepository customerReviewsRepository;

	@Autowired
	private SecUserRepository secUserRepository;

	public ResponseObject<?> getAllReviewByProductId(Long id) {

		List<CustomerReviews> customerReviewsList = customerReviewsRepository.findByProductId(id);
		List<CustomerReviewsDTO> dtoList = new ArrayList<>();

		customerReviewsList.forEach(entity -> {
			CustomerReviewsDTO dto = new CustomerReviewsDTO();
			BeanUtils.copyProperties(entity, dto);
			Blob custAvatarBlob = entity.getCustomerAvatar();
			if (custAvatarBlob != null) {
				try {
					byte[] byteImage = ImageConversion.blobToByteConversion(custAvatarBlob);
					dto.setCustomerAvatar(byteImage);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			dtoList.add(dto);
		});
		return ResponseObject.success(dtoList);
	}

	@Transactional
	public ResponseObject<?> save(CustomerReviewsDTO dto, String phoneNumber, String userName) {

		try {
			SecUser secUser = secUserRepository.findByPhoneNumberAndIsActive(phoneNumber, true);

			CustomerReviews customerEntity = ObjectMapperUtil.convertDTOToEntity(dto, CustomerReviews.class);

			if (secUser != null && secUser.getProfile() != null) {
				customerEntity.setCustomerAvatar(secUser.getProfile());
			}

			customerEntity.setCustomerName(userName);
			customerEntity.setPhoneNumber(phoneNumber);
			customerEntity.setCreatedBy(userName);

			customerReviewsRepository.save(customerEntity);

			CustomerReviewsDTO responseDTO = ObjectMapperUtil.convertEntityToDTO(customerEntity,
					CustomerReviewsDTO.class);
			Blob custAvatarBlob = customerEntity.getCustomerAvatar();
			if (custAvatarBlob != null) {
				try {
					byte[] byteImage = ImageConversion.blobToByteConversion(custAvatarBlob);
					responseDTO.setCustomerAvatar(byteImage);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return ResponseObject.success(responseDTO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	@Transactional
	public ResponseObject<?> update(CustomerReviewsDTO dto, String userName) {

		try {
			CustomerReviews customerEntity = customerReviewsRepository.findById(dto.getId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.NOT_FOUND));

			customerEntity = ObjectMapperUtil.convertDTOToEntity(customerEntity, CustomerReviews.class);

			try {
				if (dto.getCustomerAvatar() != null) {
					Blob blogAvatar = ImageConversion.byteToBlobConversion(dto.getCustomerAvatar());
					customerEntity.setCustomerAvatar(blogAvatar);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			customerEntity.setLastModifiedBy(userName);

			customerReviewsRepository.save(customerEntity);

			CustomerReviewsDTO responseDTO = ObjectMapperUtil.convertEntityToDTO(customerEntity,
					CustomerReviewsDTO.class);
			Blob custAvatarBlob = customerEntity.getCustomerAvatar();
			if (custAvatarBlob != null) {
				try {
					byte[] byteImage = ImageConversion.blobToByteConversion(custAvatarBlob);
					responseDTO.setCustomerAvatar(byteImage);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			return ResponseObject.success(responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}
}
