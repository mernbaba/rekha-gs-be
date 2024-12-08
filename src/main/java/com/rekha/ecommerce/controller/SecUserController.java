package com.rekha.ecommerce.controller;

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
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.dto.SecUserDTO;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/secuser")
public class SecUserController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	private SecUserService secUserService;

	@PutMapping("/update")
	public ResponseObject<?> updateUser(@RequestBody SecUserDTO secUserDTO) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String phoneNumber = claims.getSubject();
		String userName = (String) claims.get("userName");

		return secUserService.updateUser(secUserDTO, phoneNumber, userName);
	}

	@GetMapping("/getProfile")
	public ResponseObject<SecUserDTO> getProfile() {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String phoneNumber = claims.getSubject();

		return secUserService.getProfile(phoneNumber);

	}

	@PostMapping("/changePwd")
	private ResponseObject<?> changePassword(@RequestParam String oldPwd, @RequestParam String newPwd) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String phoneNumber = (String) claims.getSubject();
		String userName = (String) claims.get("userName");

		if (oldPwd.equalsIgnoreCase(newPwd)) {
			throw new CloudBaseException(ResponseCode.OLD_NEW_PWD_SAME);
		} else {
			return secUserService.changePassword(newPwd, phoneNumber, userName);
		}
	}

}
