package com.rekha.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.dto.SecUserDTO;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.request.JwtRequest;
import com.rekha.ecommerce.request.RazorpayRequest;
import com.rekha.ecommerce.service.OtpService;
import com.rekha.ecommerce.service.RazorpayService;
import com.rekha.ecommerce.service.SecUserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/initial")
public class JwtAuthenticationController {

	@Autowired
	SecUserService secUserService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	private RazorpayService razorpayService;

	@Autowired
	private OtpService otpService;

//	@PostMapping("/authenticate")
//	public ResponseObject<?> createAuthenticationToken(@RequestParam("phoneNumber") String phoneNumber,
//			@RequestParam("otp") String otp) {
//		System.out.println(phoneNumber);
//		try {
//			ResponseCode response = otpService.validateOtp(phoneNumber, otp);
//
//			if (response.equals(ResponseCode.SUCCESS)) {
//
//				// generate token
//				return secUserService.authenticateUser(phoneNumber);
//			} else {
//
//				throw new CloudBaseException(response);
//			}
//		} catch (CloudBaseException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new CloudBaseException(ResponseCode.BAD_REQUEST);
//		}
//
//	}

	@PostMapping("/authenticate")
	public ResponseObject<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		System.out.println(authenticationRequest.getPhoneNumber() + authenticationRequest.getPassword());

		return secUserService.authenticateUser(authenticationRequest);
	}

	@PostMapping("/forgotPassword")
	public String forgotPassword(@RequestParam("phoneNumber") String phoneNumber) {
		otpService.sendOtp(phoneNumber);
		return "OTP sent successfully to " + phoneNumber;
	}

//
	@PostMapping("/resetPassword")
	public ResponseObject<?> verifyOtpAndResetPassword(@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam String pwd, @RequestParam("otp") String otp) {

		ResponseObject<ResponseCode> response = otpService.verifyOtpAndResetPassword(phoneNumber, pwd, otp);
		return response;
	}

	@PostMapping("/register")
	public ResponseObject<?> registerUser(@RequestBody SecUserDTO secUserDTO) {
		return secUserService.saveUser(secUserDTO);
	}

//	@GetMapping("/health")
//	private ResponseEntity<String> healthCheck() {
//		return ResponseEntity.ok("Healthy");
//	}
//
//	@GetMapping("/.well-known/pki-validation/A3D7A0A49B297C764C02F9AF056B19E7.txt")
//	public ResponseEntity<String> getVerifyFile() {
//
//		String fileContent = "37ABDF3767016FC14B871C573D453688EC7DFA0B7405AEDF84E634F027DA19EB\r\n" + "comodoca.com\r\n"
//				+ "bfb61055064f7b2";
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");
//		return new ResponseEntity<String>(fileContent, headers, HttpStatus.OK);
//	}

	@PostMapping(value = "/razorPayOrder", produces = "application/json")
	public ResponseObject<?> razorPayOrder(@RequestBody RazorpayRequest razorpayRequest) throws Exception {
		return razorpayService.razorpaySave(razorpayRequest);
	}

	@PostMapping("/razorPayment")
	public Boolean createPayment(@RequestBody RazorpayRequest razorPaymentRequest) throws Exception {
		return razorpayService.createRazorpayPayment(razorPaymentRequest);
	}

}