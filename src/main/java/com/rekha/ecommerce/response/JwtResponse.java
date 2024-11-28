package com.rekha.ecommerce.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String token;
	private final String userName;
	private final String phoneNumber;
	private final String userCode;
	private final Boolean isAdmin;
	private final Boolean isFieldOfficer;
	
}