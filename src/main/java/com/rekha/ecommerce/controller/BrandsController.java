package com.rekha.ecommerce.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rekha.ecommerce.config.JwtTokenUtil;
import com.rekha.ecommerce.dto.BrandsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.service.BrandsService;
import com.rekha.ecommerce.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/brand")
public class BrandsController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	BrandsService brandsService;

	@Autowired
	SecUserService secUserService;

	@GetMapping("/getBrands")
	public ResponseObject<List<BrandsDTO>> getBrandsByCategory(@RequestParam String category) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}

		return brandsService.getBrandsByCategory(category);
	}

	@PostMapping("/save")
	public ResponseObject<BrandsDTO> saveBrand(@RequestBody BrandsDTO dto) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String username = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}

		return brandsService.save(dto, username);
	}

	@PutMapping("/update")
	public ResponseObject<BrandsDTO> updateBrand(@RequestBody BrandsDTO dto) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String username = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}

		return brandsService.update(dto, username);

	}

	@GetMapping("/getAllBrands")
	public ResponseObject<List<BrandsDTO>> getAllBrands() {
		return brandsService.getAllBrands();
	}

}
