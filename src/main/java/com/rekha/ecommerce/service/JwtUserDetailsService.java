package com.rekha.ecommerce.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.config.JwtTokenUtil;
import com.rekha.ecommerce.entity.SecUser;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.SecUserRepository;
import com.rekha.ecommerce.request.JwtRequest;
import com.rekha.ecommerce.response.JwtResponse;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private SecUserRepository secUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		SecUser user = secUserRepository.findByPhoneNumber(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid User");
		}
		return new org.springframework.security.core.userdetails.User(user.getPhoneNumber(), user.getPassword(),
				new ArrayList<>());
	}

	public ResponseEntity<JwtResponse> getToken(JwtRequest authenticationRequest) {
		JwtResponse response = generateJwtToken(authenticationRequest);
		return ResponseEntity.ok(response);
	}

	public JwtResponse generateJwtToken(JwtRequest authenticationRequest) {
		SecUser user = secUserRepository.findByPhoneNumber(authenticationRequest.getPhoneNumber());
		validateUser(user);

		UserDetails userDetails = loadUserByUsername(authenticationRequest.getPhoneNumber());
		String token = jwtTokenUtil.generateToken(userDetails, user);

		JwtResponse jwtResponse = new JwtResponse(token,user.getUserName(),user.getPhoneNumber(),user.getUserCode(),user.getIsAdmin(),user.getIsFieldOfficer());

		return jwtResponse;
	}

	private void validateUser(SecUser user) {
		if (user == null) {
			throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
		}

	}
}