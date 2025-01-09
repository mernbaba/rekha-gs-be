package com.rekha.ecommerce.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.Otp;
import com.rekha.ecommerce.entity.SecUser;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.OtpRepository;
import com.rekha.ecommerce.repository.SecUserRepository;

import jakarta.transaction.Transactional;

@Service
public class OtpService {

	@Autowired
	private SecUserRepository secUserRepository;

	@Autowired
	private OtpRepository otpRepository;
	
	private PasswordEncoder passwordEncoder;

	/*@Value("${twilio.account-sid}")
	private String accountSid;
	
	@Value("${twilio.auth-token}")
	private String authToken;
	
	@Value("${twilio.phone-number}")
	private String fromPhoneNumber;
	
	public OtpService() {
		Twilio.init(accountSid, authToken);
	}
	*/

	@Value("${textlocal.api.key}")
	private String txtApiKey;

	@Value("${textlocal.sender.id}")
	private String senderId;
	
	@Value("${textlocal.template.id}")
	private String templateId;

//	private static final String API_URL = "https://api.textlocal.in/send/";

	private static final SecureRandom secureRandom = new SecureRandom();

	public String generateOTP() {
		int otp = 100000 + secureRandom.nextInt(900000); // 6-digit OTP
		return String.valueOf(otp);
	}

	@Transactional
	public ResponseObject<?> sendOtp(String phoneNumber) {

		try {
			ResponseCode response = null;

			Otp otpEntity = new Otp();
//			Messages msg = new Messages();

			SecUser secUser = secUserRepository.findByPhoneNumber(phoneNumber);
			
			if(!ObjectUtils.isEmpty(secUser)) {
				// taking random 6 char
				String OTP = generateOTP();

				otpEntity.setOtp(OTP);
				otpEntity.setPhoneNumber(phoneNumber);
				otpEntity.setOtpGentime(LocalDateTime.now());
				otpEntity.setOtpExptime(LocalDateTime.now().plusMinutes(10));
				otpEntity.setOtpStatus("GENERATED");
				otpEntity.setCreatedby("system");
				otpEntity.setCreateddate(LocalDateTime.now());

				/*if (secUser == null && ObjectUtils.isEmpty(secUser)) {
					
					// using this code to send OTP for registration
					
					messageBody = "Your OTP is " + OTP
					+ " . Use this to register your mobile number on Rekha. Valid for 10 minutes.";
					
					response = ResponseCode.OTP_SENT_TO_YOUR_MOBILE;
					} else {
					// use this to send OTP for other requirement
				}*/

				String messageBody = "Your OTP is "+OTP+". Use this to set new password. Valid for 10 minutes - Rekha Seeds and Pesticides";

				save(otpEntity);

//				sendSms(phoneNumber, messageBody);
//				String otpResponse = sendSMS("91" + phoneNumber, messageBody);
				String otpResponse = sendSMS(phoneNumber, messageBody);
				System.out.println("Response from txt local ::" + otpResponse);
				return ResponseObject.success(response);
			}else {
				throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
			}

		} catch (Exception e) {
			throw e;
		}

	}

//	public String sendSMS(String phoneNumber, String messageBody) {
//		try {
//			// Construct the API request data
//			String apiKey = "apikey=" + txtApiKey;
//			String message = "&message=" + messageBody;
//			String sender = "&sender=" + senderId; // Sender ID
//			String numbers = "&numbers=" + phoneNumber; // Recipient's phone number
//			String template = "&template_id=" + templateId;
//
//			// Open connection to Textlocal API
//			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
//			String data = apiKey + numbers + message + sender + template; // Combine all parameters
//			conn.setDoOutput(true);
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
//			conn.getOutputStream().write(data.getBytes("UTF-8"));
//
//			// Read response from Textlocal
//			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			StringBuffer responseBuffer = new StringBuffer();
//			String line;
//			while ((line = rd.readLine()) != null) {
//				responseBuffer.append(line);
//			}
//			rd.close();
//
//			// Return the response from Textlocal API
//			return responseBuffer.toString();
//		} catch (Exception e) {
//			System.out.println("Error SMS: " + e);
//			return "Error: " + e.getMessage();
//		}
//	}
	
	public String sendSMS(String phoneNumber, String messageBody) {
		
		ResponseCode error = null;
		
		try {
			// Construct the API request data
			String apiKey = "apiKey=" + txtApiKey;
			String message = "&message=" + URLEncoder.encode(messageBody, "UTF-8");
			String sender = "&sender=" + senderId; // Sender ID
			String numbers = "&numbers=" + URLEncoder.encode(phoneNumber, "UTF-8"); // Recipient's phone number
			
			String apiUrl = "https://api.textlocal.in/send/?" + apiKey + message + sender + numbers;
			
			URL url = new URL(apiUrl);
			URLConnection conn = url.openConnection();

			// Open connection to Textlocal API
//			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
//			String data = apiKey + numbers + message /* + sender */; // Combine all parameters
			conn.setDoOutput(true);
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
//			conn.getOutputStream().write(data.getBytes("UTF-8"));

			// Read response from Textlocal
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			StringBuffer responseBuffer = new StringBuffer();
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = rd.readLine()) != null) {
//				responseBuffer.append(line);
				sb.append(line).append("\n");
			}
			System.err.println(sb.toString());
//			rd.close();
			JSONObject jsonResponse = new JSONObject(sb.toString());
			if(!jsonResponse.optString("status").equalsIgnoreCase("success")) {
				error = ResponseCode.FAILED_TO_SEND_OTP;
				throw new CloudBaseException(error);
			}
			return sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			if(error != null) {
				throw new CloudBaseException(error);
			}else {
				throw new CloudBaseException(ResponseCode.UNKNOWN_ERROR_OCCURRED);
			}
		}
	}

	/*public String sendSMS(String phoneNumber, String messageBody) {
		try {
			// Construct data
			String apiKey = "apikey=" + txtApiKey;
			String message = "&message=" + messageBody;
	//			String sender = "&sender=" + "TXTLCL";
			String sender = "&sender=" + senderId;
			String numbers = "&numbers=" + phoneNumber;
	
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
	
			return stringBuffer.toString();
		} catch (Exception e) {
			System.out.println("Error SMS " + e);
			return "Error " + e;
		}
	}*/

	/*public String sendSMS(String phoneNumber, String message) {
		RestTemplate restTemplate = new RestTemplate();
	
	//        // Prepare the OTP message
	//        String message = "Your OTP is: " + otp;
	
		// Set up parameters for the API request
		Map<String, String> params = new HashMap<>();
		params.put("apikey", apiKey); // Your Textlocal API Key
		params.put("numbers", phoneNumber); // Recipient phone number
		params.put("message", message); // OTP message
		params.put("sender", "TXTLCL"); // Sender ID (your brand name or numeric code)
	
		// Make the API request
		String response = restTemplate.postForObject(API_URL, params, String.class);
	
		return response; // Return the response for further processing (success or failure)
	}*/

	/*// Using TWILIO sms server
	public void sendSms(String toPhoneNumber, String messageBody) {
		try {
	
			Message message = Message.creator(new PhoneNumber(toPhoneNumber), // To phone number
					new PhoneNumber(fromPhoneNumber), // From phone number (your Twilio number)
					messageBody // Message body
			).create();
	
			System.out.println("Message sent with SID: " + message.getSid());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}*/

	@Transactional
	public Otp save(Otp otp) {
		try {

			return otpRepository.save(otp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	public ResponseObject<ResponseCode> verifyOtpAndResetPassword(String phoneNumber, String pwd, String otp) {
		try {
			SecUser secUser = secUserRepository.findByPhoneNumberAndIsActive(phoneNumber, true);
			if (secUser != null && ObjectUtils.isNotEmpty(secUser)) {

				Otp otpEntity = otpRepository.findByPhoneNumberforOtp(phoneNumber);

				LocalDateTime currentDate = LocalDateTime.now();
				LocalDateTime exp = otpEntity.getOtpExptime();

				LocalTime currentTime = currentDate.toLocalTime();
				LocalTime expTime = exp.toLocalTime();

				if (currentTime.isAfter(expTime)) {

					otpRepository.updateOtpStatusByIdAndPartyCode("EXPIRED", otpEntity.getId());
					throw new CloudBaseException(ResponseCode.OTP_EXPIRED);
				}

				if (otpEntity.getOtp().equalsIgnoreCase(otp)) {
					secUserRepository.updatePassword(secUser.getPhoneNumber(), passwordEncoder.encode(pwd), secUser.getUserName());
					otpRepository.updateOtpStatusByIdAndPartyCode("SUCCESS", otpEntity.getId());
					return new ResponseObject<>(ResponseCode.SUCCESS);
				} else {
					throw new CloudBaseException(ResponseCode.INVALID_OTP);
				}
			} else {
				throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
			}

		} catch (CloudBaseException exp) {
			exp.printStackTrace();
			throw exp;
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.UNKNOWN_ERROR_OCCURRED);
		}
	}

	public String generateJwtTokenIfOtpValid(String phoneNumber, String otp) {
		return null;
	}

}
