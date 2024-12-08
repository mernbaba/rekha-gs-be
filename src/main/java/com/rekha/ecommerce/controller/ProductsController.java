package com.rekha.ecommerce.controller;

import java.util.List;

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
import com.rekha.ecommerce.dto.ProductQuantityDTO;
import com.rekha.ecommerce.dto.ProductsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.service.ProductsService;
import com.rekha.ecommerce.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

	@Autowired
	private ProductsService productsService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private SecUserService secUserService;

	@PostMapping("/save")
	public ResponseObject<List<ProductsDTO>> saveProducts(@RequestBody List<ProductsDTO> productsDTOs) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}
		return productsService.saveProducts(productsDTOs, userName);
	}

	@PutMapping("/update")
	public ResponseObject<ProductsDTO> update(@RequestBody ProductsDTO productsDTO) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String userName = (String) claims.get("userName");
		String phoneNumber = claims.getSubject();

		if (!secUserService.hasPermissions(phoneNumber)) {
			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
		}
		return productsService.update(productsDTO, userName);
	}

	@GetMapping("/getProducts/{brand}")
	public ResponseObject<List<ProductsDTO>> getProductsByBrand(@PathVariable String brand) {

//		String token = request.getHeader("Authorization");
//		token = StringUtils.replace(token, "Bearer ", "");
//
//		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
//		String phoneNumber = claims.getSubject();
//		if (!secUserService.hasPermissions(phoneNumber)) {
//			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
//		}
		return productsService.getProductsByBrand(brand);
	}

	@PostMapping("/getProductsByQuantity")
	public ResponseObject<List<ProductsDTO>> getProducts(@RequestBody List<ProductQuantityDTO> dtoList) {
		return productsService.getProductsByQuantity(dtoList);
	}

}
