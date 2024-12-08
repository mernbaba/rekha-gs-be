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
import com.rekha.ecommerce.dto.BlogDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.service.BlogService;
import com.rekha.ecommerce.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	private SecUserService secUserService;

	@Autowired
	private BlogService blogService;

	@GetMapping("/getAllBlogs")
	public ResponseObject<?> getAllBlogs() {

		return blogService.getAllBlogs();
	}

	@PostMapping("/save")
	public ResponseObject<?> saveBlog(@RequestBody BlogDTO dto) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = tokenUtil.getAllClaimsFromToken(token);
//		String userCode = (String) claims.get("userCode");
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber) && !secUserService.hasPermissionsForFO(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}

		return blogService.save(dto, phoneNumber, userName);
	}

	@PutMapping("/update")
	public ResponseObject<?> updateBlog(@RequestBody BlogDTO dto) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = tokenUtil.getAllClaimsFromToken(token);
//		String userCode = (String) claims.get("userCode");
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber) && !secUserService.hasPermissionsForFO(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}

		return blogService.update(dto, phoneNumber, userName);
	}

}
