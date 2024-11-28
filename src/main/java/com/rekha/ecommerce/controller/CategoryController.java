package com.rekha.ecommerce.controller;

import java.util.List;

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
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.service.CategoryService;
import com.rekha.ecommerce.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SecUserService secUserService;

	@PostMapping("/save")
	public ResponseObject<CategoryDTO> categorySave(@RequestBody CategoryDTO categoryDTO) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = tokenUtil.getAllClaimsFromToken(token);
//		String userCode = (String) claims.get("userCode");
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();
		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}

		return categoryService.save(categoryDTO, userName);
	}

	@PutMapping("/update")
	public ResponseObject<CategoryDTO> categoryUpdate(@RequestBody CategoryDTO categoryDTO) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = tokenUtil.getAllClaimsFromToken(token);
//		String userCode = (String) claims.get("userCode");
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();
		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}

		return categoryService.update(categoryDTO, userName);
	}

	@GetMapping("/getAll")
	public ResponseObject<List<CategoryDTO>> getAllCategories() {

		return categoryService.getAllCategories();
	}

}
