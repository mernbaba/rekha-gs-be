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
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.request.CustomerTicketsRequest;
import com.rekha.ecommerce.service.CustomerTicketsService;
import com.rekha.ecommerce.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tickets")
public class CustomerTicketsController {

	@Autowired
	private CustomerTicketsService customerTicketsService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	SecUserService secUserService;

	@PostMapping("/save")
	public ResponseObject<CustomerTicketsRequest> save(@RequestBody CustomerTicketsRequest customerTicketsRequest) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = tokenUtil.getAllClaimsFromToken(token);
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		return customerTicketsService.saveTicket(customerTicketsRequest, userName, phoneNumber);
	}

	@PutMapping("/update")
	public ResponseObject<CustomerTicketsRequest> update(@RequestBody CustomerTicketsRequest customerTicketsRequest) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = tokenUtil.getAllClaimsFromToken(token);
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();
		if (!secUserService.hasPermissions(phoneNumber) && !secUserService.hasPermissionsForFO(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}

		return customerTicketsService.updateTicket(customerTicketsRequest, userName);
	}

	// Get Tickets
	@GetMapping("/gettickets")
	public ResponseObject<List<CustomerTicketsRequest>> getTickets() {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = tokenUtil.getAllClaimsFromToken(token);
//		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();
		return customerTicketsService.getTickets(phoneNumber);
	}

}
