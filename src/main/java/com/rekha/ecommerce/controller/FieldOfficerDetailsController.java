package com.rekha.ecommerce.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rekha.ecommerce.config.JwtTokenUtil;
import com.rekha.ecommerce.dto.FieldOfficerDetailsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.service.FieldOfficerDetailsService;
import com.rekha.ecommerce.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/field/officer")
public class FieldOfficerDetailsController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	FieldOfficerDetailsService fieldOfficerDetailsService;

	@Autowired
	SecUserService secUserService;

	@GetMapping("/getFieldOfficers")
	public ResponseObject<?> getAllFieldsOfficers() {
		return fieldOfficerDetailsService.getAll();

	}

	@PostMapping("/save")
	public ResponseObject<?> saveFieldOfficer(@RequestBody FieldOfficerDetailsDTO dto) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String username = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}

		return fieldOfficerDetailsService.save(dto, username);
	}

	@PutMapping("/update")
	public ResponseObject<?> updateFieldOfficer(@RequestBody FieldOfficerDetailsDTO dto) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String username = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}

		return fieldOfficerDetailsService.update(dto, username);
	}

}
