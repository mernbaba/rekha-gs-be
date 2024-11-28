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
import com.rekha.ecommerce.dto.OrderDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.request.OrdersRequest;
import com.rekha.ecommerce.service.OrderService;
import com.rekha.ecommerce.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	@Autowired
    HttpServletRequest request;

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    SecUserService secUserService;
    
    @Autowired
    OrderService orderService;
	
	
	@GetMapping("/getAll")
	public ResponseObject<?> getAllOrders() {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}
		
		return orderService.getAll();

	}
	
	@GetMapping("/getOrdersByStatus")
	public ResponseObject<?> getAllOrdersByStatus(@RequestParam String status) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}
		
		return orderService.getOrdersByStatus(status);

	}
	
	@GetMapping("/getUserOrders")
	public ResponseObject<?> getUserOrders(){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String phoneNumber = claims.getSubject();
		
		return orderService.getUserOrders(phoneNumber);
	}
	
	@PostMapping("/save")
	public ResponseObject<?> save(@RequestBody OrdersRequest dto) {
		
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String phoneNumber = claims.getSubject();
		String username = (String)claims.get("userName");
		
		return orderService.save(dto, phoneNumber, username);
		
	}
	
	@PutMapping("/update")
	public ResponseObject<?> update(@RequestBody OrdersRequest orderRequest) {
		
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String phoneNumber = claims.getSubject();
		String username = (String)claims.get("userName");
		
		return orderService.update(orderRequest, phoneNumber, username);
		
	}
	
	

}
