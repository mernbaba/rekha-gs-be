package com.rekha.ecommerce.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rekha.ecommerce.config.JwtTokenUtil;
import com.rekha.ecommerce.dto.CustomerReviewsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.service.CustomerReviewsService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/reviews")
public class CustomerReviewsController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	private CustomerReviewsService customerReviewsService;

	@GetMapping("/getReviewsByProduct/{id}")
	public ResponseObject<?> getReviewsByProduct(@PathVariable Long id) {

//		String token = request.getHeader("Authorization");
//		token = StringUtils.replace(token, "Bearer ", "");
//		Claims claims = tokenUtil.getAllClaimsFromToken(token);

//		String userCode = (String) claims.get("userCode");
//		String userName = (String) claims.get("userName");
//		String phoneNumber = claims.getSubject();

		return customerReviewsService.getAllReviewByProductId(id);

	}

	@PostMapping("/saveReview")
	public ResponseObject<?> saveCustomerReview(@RequestBody CustomerReviewsDTO dto) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = tokenUtil.getAllClaimsFromToken(token);

//		String userCode = (String) claims.get("userCode");
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		return customerReviewsService.save(dto, phoneNumber, userName);

	}

	@PutMapping("UpdateReview")
	public ResponseObject<?> updateCustomerReview(@RequestBody CustomerReviewsDTO dto) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = tokenUtil.getAllClaimsFromToken(token);

//		String userCode = (String) claims.get("userCode");
		String userName = (String) claims.get("userName");
//		String phoneNumber = claims.getSubject();

		return customerReviewsService.update(dto, userName);
	}

}
