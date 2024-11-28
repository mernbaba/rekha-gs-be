//package com.rekha.ecommerce.service;
//
//import java.security.SecureRandom;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//import org.apache.commons.lang3.ObjectUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.rekha.ecommerce.dto.ResponseObject;
//import com.rekha.ecommerce.entity.Otp;
//import com.rekha.ecommerce.entity.SecUser;
//import com.rekha.ecommerce.enumeration.ResponseCode;
//import com.rekha.ecommerce.exception.CloudBaseException;
//import com.rekha.ecommerce.repository.OtpRepository;
//import com.rekha.ecommerce.repository.SecUserRepository;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//
//import jakarta.transaction.Transactional;
//
//@Service
//public class OtpService {
//
//	@Autowired
//	private SecUserRepository secUserRepository;
//
//	@Autowired
//	private OtpRepository otpRepository;
//
//	@Value("${twilio.account-sid}")
//	private String accountSid;
//
//	@Value("${twilio.auth-token}")
//	private String authToken;
//
//	@Value("${twilio.phone-number}")
//	private String fromPhoneNumber;
//
//	public OtpService() {
//		Twilio.init(accountSid, authToken);
//	}
//
//	private static final SecureRandom secureRandom = new SecureRandom();
//
//	public String generateOTP() {
//		int otp = 100000 + secureRandom.nextInt(900000); // 6-digit OTP
//		return String.valueOf(otp);
//	}
//
//	public ResponseObject<?> sendOtp(String phoneNumber) {
//
//		try {
//			ResponseCode response = null;
//			String messageBody = null;
//
//			Otp otpEntity = new Otp();
////			Messages msg = new Messages();
//
//			SecUser secUser = secUserRepository.findByPhoneNumber(phoneNumber);
//
//			// taking random 6 char
//			String OTP = generateOTP();
//
//			otpEntity.setOtp(OTP);
//			otpEntity.setOtpGentime(LocalDateTime.now());
//			otpEntity.setOtpExptime(LocalDateTime.now().plusMinutes(10));
//			otpEntity.setOtpStatus("GENERATED");
//			otpEntity.setCreatedby("system");
//			otpEntity.setCreateddate(LocalDateTime.now());
//
//			if (secUser == null && ObjectUtils.isEmpty(secUser)) {
//				// using this code to send OTP for registration
//
//				messageBody = "Your OTP is " + OTP
//						+ " . Use this to register your mobile number on Rekha. Valid for 10 minutes.";
//
//				response = ResponseCode.OTP_SENT_TO_YOUR_MOBILE;
//			} else {
//				// use this to send OTP for other requirement
//			}
//
//			save(otpEntity);
//
//			sendSms(phoneNumber, messageBody);
//
//			return new ResponseObject<>(response);
//
//		} catch (Exception e) {
//			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
//		}
//
//	}
//
//	public void sendSms(String toPhoneNumber, String messageBody) {
//		try {
//
//			Message message = Message.creator(new PhoneNumber(toPhoneNumber), // To phone number
//					new PhoneNumber(fromPhoneNumber), // From phone number (your Twilio number)
//					messageBody // Message body
//			).create();
//
//			System.out.println("Message sent with SID: " + message.getSid());
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
//		}
//	}
//
//	@Transactional
//	public Otp save(Otp otp) {
//		try {
//
//			return otpRepository.save(otp);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
//		}
//	}
//
//	public ResponseCode validateOtp(String phoneNumber, String enteredOtp) {
//
//		Otp otpEntity = otpRepository.findByPhoneNumberforOtp(phoneNumber);
//
//		LocalDateTime currentDate = LocalDateTime.now();
//		LocalDateTime exp = otpEntity.getOtpExptime();
//
//		LocalTime currentTime = currentDate.toLocalTime();
//		LocalTime expTime = exp.toLocalTime();
//
//		if (currentTime.isAfter(expTime)) {
//
//			otpRepository.updateOtpStatusByIdAndPartyCode("EXPIRED", otpEntity.getId());
//			return ResponseCode.OTP_EXPIRED;
//		}
//
//		if (otpEntity.getOtp().equalsIgnoreCase(enteredOtp)) {
//
//			otpRepository.updateOtpStatusByIdAndPartyCode("SUCCESS", otpEntity.getId());
//			return ResponseCode.SUCCESS;
//
//		} else {
//			return ResponseCode.INVALID_OTP;
//		}
//	}
//
//	public String generateJwtTokenIfOtpValid(String phoneNumber, String otp) {
//		return null;
//	}
//
//}
