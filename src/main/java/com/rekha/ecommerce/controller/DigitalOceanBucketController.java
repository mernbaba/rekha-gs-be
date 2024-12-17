package com.rekha.ecommerce.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rekha.ecommerce.config.JwtTokenUtil;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.service.DigitalOceanSpacesService;
import com.rekha.ecommerce.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/file")
@CrossOrigin
public class DigitalOceanBucketController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	SecUserService secUserService;

	@Autowired
	private DigitalOceanSpacesService digitalOceanSpacesService;

	@GetMapping("/getFiles")
	public ResponseObject<List<Map<String, Object>>> downloadFile(@RequestParam List<Long> fileIds) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);

		try {
			List<Map<String, Object>> response = digitalOceanSpacesService.fetchDoFiles(fileIds);

			return ResponseObject.success(response);

		} catch (IOException e) {
			throw new CloudBaseException(ResponseCode.UNKNOWN_ERROR_OCCURRED);
		}
	}

	@PostMapping("/save")
	public ResponseObject<?> uploadImageFileToDoBucket(@RequestPart String fileType, @RequestPart MultipartFile[] files)
			throws Exception {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String username = (String) claims.get("userName");
//		String phoneNumber = claims.getSubject();

//		if (!secUserService.hasPermissions(phoneNumber)) {
//			throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
//		}

		return digitalOceanSpacesService.uploadImage(fileType, files, username);
	}

	@DeleteMapping("/deleteFile")
	public ResponseObject<Object> deletefileFromDOSpaces(@RequestParam Long fileId) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);

		return digitalOceanSpacesService.deleteFileFromDO(fileId);
	}

}
