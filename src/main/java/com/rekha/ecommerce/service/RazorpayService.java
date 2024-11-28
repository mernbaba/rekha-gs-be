package com.rekha.ecommerce.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.OrderClient;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.rekha.ecommerce.dto.PaymentGatewayDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.PaymentGateway;
import com.rekha.ecommerce.repository.PaymentGatewayRepository;
import com.rekha.ecommerce.request.RazorpayRequest;

import jakarta.transaction.Transactional;

@Service
public class RazorpayService {

	@Value("${razorpay.key.id}")
	private String razorKey;

	@Value("${razorpay.key.secret}")
	private String razorSecrectKey;

	@Autowired
	private PaymentGatewayRepository paymentGatewayRepository;

	@Transactional
	public ResponseObject<?> razorpaySave(RazorpayRequest razorpayRequest) throws Exception {

		RazorpayClient razorpay = new RazorpayClient(razorKey, razorSecrectKey);
		try {
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", razorpayRequest.getAmount()/* .multiply(BigDecimal.valueOf(100)) */);
			orderRequest.put("currency", "INR");

			Order order = razorpay.orders.create(orderRequest);

			OrderClient orders = razorpay.orders;

			Map<String, String> map = new HashMap<>();
			map.put("id", order.get("id"));
			System.err.println("order 2:: " + orders.fetch(order.get("id")));
			Order ord = orders.fetch(order.get("id"));

			PaymentGateway gatewayOrders = new PaymentGateway();
			Integer orderAmount = ord.get("amount");
			orderAmount /= 100;

			gatewayOrders.setAmount(BigDecimal.valueOf(orderAmount));
			gatewayOrders.setAttempts(ord.get("attempts"));
			gatewayOrders.setCreateddate(new Date());
			gatewayOrders.setCurrency(ord.get("currency"));
			gatewayOrders.setGatewayCreatedDate(ord.get("created_at"));
			gatewayOrders.setOrderId(ord.get("id"));
			gatewayOrders.setRefType(ord.get("entity"));
			String status = ord.get("status");
			gatewayOrders.setStatus(status.toUpperCase());
			gatewayOrders.setGateway("RAZORPAY");
			paymentGatewayRepository.save(gatewayOrders);

			PaymentGatewayDTO gatewayOrdersDTO = new PaymentGatewayDTO();
			BeanUtils.copyProperties(gatewayOrders, gatewayOrdersDTO);

			return ResponseObject.success(gatewayOrdersDTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Transactional
	public Boolean createRazorpayPayment(RazorpayRequest razorPaymentRequest) throws Exception {
//		RazorpayClient razorpay = new RazorpayClient(razorKey, razorSecrectKey);
		try {
			JSONObject options = new JSONObject();
			options.put("razorpay_order_id", razorPaymentRequest.getOrderId());
			options.put("razorpay_payment_id", razorPaymentRequest.getPaymentId());
			options.put("razorpay_signature", razorPaymentRequest.getRazorSecKey());

			PaymentGateway gatewayOrders = paymentGatewayRepository.findAllByOrderId(razorPaymentRequest.getOrderId());
			boolean status = Utils.verifyPaymentSignature(options, razorSecrectKey);
//			gatewayOrders.setGatewaySignature(razorPaymentRequest.getRazorSecKey());
			gatewayOrders.setPaymentId(razorPaymentRequest.getPaymentId());
			if (status) {
				gatewayOrders.setStatus("SUCCESS");
			} else {
				gatewayOrders.setStatus("FAILED");
			}
			paymentGatewayRepository.save(gatewayOrders);
			PaymentGatewayDTO gatewayOrdersDTO = new PaymentGatewayDTO();
			BeanUtils.copyProperties(gatewayOrders, gatewayOrdersDTO);

			return status;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
