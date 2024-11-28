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
import com.rekha.ecommerce.dto.CategoryDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.dto.UserAddressDetailsDTO;
import com.rekha.ecommerce.service.UserAddressDetailsService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user/address")
public class UserAddressDetailsController {

	@Autowired
	private UserAddressDetailsService addressDetailsService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtTokenUtil tokenUtil;

	@PostMapping("/save")
	public ResponseObject<UserAddressDetailsDTO> save(@RequestBody UserAddressDetailsDTO detailsDTO) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = tokenUtil.getAllClaimsFromToken(token);
//		String userCode = (String) claims.get("userCode");
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();
		return addressDetailsService.save(detailsDTO, phoneNumber, userName);
	}

	@PutMapping("/update")
	public ResponseObject<UserAddressDetailsDTO> updateAddressDetails(@RequestBody UserAddressDetailsDTO addressDetailsDTO) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = tokenUtil.getAllClaimsFromToken(token);
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		return addressDetailsService.update(addressDetailsDTO, userName, phoneNumber);
	}

	@GetMapping("/userAddress")
	public ResponseObject<?> getUserAddress() {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = tokenUtil.getAllClaimsFromToken(token);
//		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		return addressDetailsService.getUserAddress(phoneNumber);
	}

}
