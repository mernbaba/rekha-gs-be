package com.rekha.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.FieldOfficerDetailsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.FieldOfficerDetails;
import com.rekha.ecommerce.entity.SecUser;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.FieldOfficeDetailsRepository;
import com.rekha.ecommerce.repository.SecUserRepository;

import jakarta.transaction.Transactional;

@Service
public class FieldOfficerDetailsService {

	@Autowired
	FieldOfficeDetailsRepository fieldOfficeDetailsRepository;

	@Autowired
	private SecUserRepository secUserRepository;

	public ResponseObject<?> getAll() {
		List<FieldOfficerDetailsDTO> dtoList = new ArrayList<FieldOfficerDetailsDTO>();

		fieldOfficeDetailsRepository.findAll().forEach(entity -> {
			FieldOfficerDetailsDTO dto = new FieldOfficerDetailsDTO();
			BeanUtils.copyProperties(entity, dto);
			dtoList.add(dto);
		});

		return ResponseObject.success(dtoList);
	}

	public ResponseObject<?> save(FieldOfficerDetailsDTO dto, String username) {

		try {
			FieldOfficerDetails entity = fieldOfficeDetailsRepository.findByPhoneNumber(dto.getPhoneNumber());
			if (entity != null && ObjectUtils.isNotEmpty(entity)) {
				throw new CloudBaseException(ResponseCode.DUPLICATE_ENTRY);
			}
			entity = new FieldOfficerDetails();
			BeanUtils.copyProperties(dto, entity);

			entity.setCreatedBy(username);

			fieldOfficeDetailsRepository.save(entity);

			FieldOfficerDetailsDTO response = new FieldOfficerDetailsDTO();
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

	public ResponseObject<?> update(FieldOfficerDetailsDTO dto, String username) {

		try {
			FieldOfficerDetails entity = fieldOfficeDetailsRepository.findById(dto.getId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.USER_NOT_FOUND));
			BeanUtils.copyProperties(dto, entity);

			entity.setLastModifiedBy(username);

			fieldOfficeDetailsRepository.save(entity);

			FieldOfficerDetailsDTO response = new FieldOfficerDetailsDTO();
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

	@Transactional
	public ResponseObject<?> deleteFieldOfficerById(Long id) {

		Optional<FieldOfficerDetails> details = fieldOfficeDetailsRepository.findById(id);
		if (details != null && ObjectUtils.isNotEmpty(details)) {
			SecUser secUser = secUserRepository.findByPhoneNumber(details.get().getPhoneNumber());

			fieldOfficeDetailsRepository.deleteById(id);

			if (secUser != null && ObjectUtils.isNotEmpty(secUser)) {
				secUserRepository.deleteById(secUser.getId());
			}
			return new ResponseObject<>(ResponseCode.SUCCESS);
		} else {
			throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
		}

	}

}
