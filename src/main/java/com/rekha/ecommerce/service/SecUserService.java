package com.rekha.ecommerce.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.dto.SecUserDTO;
import com.rekha.ecommerce.entity.SecUser;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.FieldOfficeDetailsRepository;
import com.rekha.ecommerce.repository.SecUserRepository;
import com.rekha.ecommerce.request.JwtRequest;
import com.rekha.ecommerce.response.JwtResponse;

import jakarta.transaction.Transactional;

@Service
public class SecUserService {

	@Autowired
	private SecUserRepository secUserRepository;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private FieldOfficeDetailsRepository fieldOfficeDetailsRepository;

	@Autowired
	private PasswordEncoder encoder;

//	private final List<String> adminPhoneNumbers = Arrays.asList("9133731712","7013764222");

	@Value("#{'${admin.phone.numbers}'.split(',')}")
	private List<String> adminPhoneNumbers;

	@Transactional
	public ResponseObject<?> saveUser(SecUserDTO secUserDTO) {
		try {

			SecUser secUserEntity = secUserRepository.findByPhoneNumber(secUserDTO.getPhoneNumber());
			if (secUserEntity != null && ObjectUtils.isNotEmpty(secUserEntity)) {
				throw new CloudBaseException(ResponseCode.DUPLICATE_ENTRY);
			}
			secUserEntity = new SecUser();
			BeanUtils.copyProperties(secUserDTO, secUserEntity);
			secUserEntity.setPassword(encoder.encode(secUserEntity.getPassword()));
			if (adminPhoneNumbers.contains(secUserEntity.getPhoneNumber())) {
				secUserEntity.setIsAdmin(true);
			}
			if (fieldOfficeDetailsRepository.findByPhoneNumber(secUserDTO.getPhoneNumber()) != null) {
				secUserEntity.setIsFieldOfficer(true);
			}

			secUserEntity.setCreatedBy("system");
			secUserEntity.setUserCode("");
			secUserEntity.setIsActive(true);
			secUserEntity = secUserRepository.save(secUserEntity);

			secUserEntity.setUserCode(generateShortCode(secUserEntity.getUserName()) + secUserEntity.getId());
			secUserEntity = secUserRepository.save(secUserEntity);

			SecUserDTO response = new SecUserDTO();
			BeanUtils.copyProperties(secUserEntity, response);
			response.setPassword(null);
			return ResponseObject.success(response);

		} catch (CloudBaseException exp) {
			exp.printStackTrace();
			throw exp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	public ResponseObject<?> authenticateUser(JwtRequest authenticationRequest) throws Exception {

		SecUser secUser = secUserRepository.findByPhoneNumber(authenticationRequest.getPhoneNumber());
		if (secUser == null) {
			System.out.println(ResponseCode.USER_NOT_FOUND);
			throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
		}
		boolean check = encoder.matches(authenticationRequest.getPassword(), secUser.getPassword());

		if (check) {
			JwtResponse jwtresponse = jwtUserDetailsService.getToken(authenticationRequest).getBody();

			System.out.println(jwtresponse);
			return new ResponseObject<>(jwtresponse, ResponseCode.SUCCESS);
		} else {
			throw new CloudBaseException(ResponseCode.INVALID_CREDENTIALS);
		}

	}

	public String generateShortCode(String name) {
		name = name.trim();

		// Split the full name into words by spaces
		String[] words = name.split("\\s+");

		StringBuilder shortCode = new StringBuilder();

		if (words.length > 1) {
			for (String word : words) {
				if (!word.isEmpty()) {
					shortCode.append(word.substring(0, 1).toUpperCase());
				}
			}
		} else {
			String word = words[0];
			if (word.length() >= 2) {
				shortCode.append(word.substring(0, 2).toUpperCase());
			} else {
				shortCode.append(word.toUpperCase());
			}
		}
		return shortCode + "-".toString();
	}

	public boolean hasPermissions(String phoneNumber) {
		SecUser secUser = secUserRepository.findByPhoneNumber(phoneNumber);

		if (secUser != null && secUser.getIsAdmin()) {
			return true;
		}
		return false;
	}
	
	
	public boolean hasPermissionsForFO(String phoneNumber) {
		SecUser secUser = secUserRepository.findByPhoneNumber(phoneNumber);

		if (secUser != null && secUser.getIsFieldOfficer()) {
			return true;
		}
		return false;
	}

	public ResponseObject<SecUserDTO> getProfile(String phoneNumber) {

		SecUser secUser = secUserRepository.findByPhoneNumber(phoneNumber);
		if (secUser != null && ObjectUtils.isNotEmpty(secUser)) {
			SecUserDTO dto = new SecUserDTO();
			BeanUtils.copyProperties(secUser, dto);
			dto.setPassword(null);
			return ResponseObject.success(dto);
		} else {
			throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
		}

	}

}
