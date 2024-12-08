package com.rekha.ecommerce.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rekha.ecommerce.config.JwtTokenUtil;
import com.rekha.ecommerce.dto.FarmerDetailsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.service.FarmerDetailsService;
import com.rekha.ecommerce.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/farmer/details")
public class FarmerDetailsController {

	@Autowired
	private FarmerDetailsService farmerDetailsService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	SecUserService secUserService;

	@PostMapping("/save")
	public ResponseObject<FarmerDetailsDTO> farmerSave(@RequestBody FarmerDetailsDTO farmerDetailsDTO) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String username = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber) && !secUserService.hasPermissionsForFO(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}
		return farmerDetailsService.saveFarmer(farmerDetailsDTO, username);
	}
	
	
	@PostMapping("/update")
	public ResponseObject<FarmerDetailsDTO> farmerUpdate(@RequestBody FarmerDetailsDTO farmerDetailsDTO) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String username = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber) && !secUserService.hasPermissionsForFO(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}
		return farmerDetailsService.updateFarmer(farmerDetailsDTO, username);
	}
	
	@GetMapping("/getall")
	public ResponseObject<?> getAll(){
		return farmerDetailsService.getAll();
	}

}
