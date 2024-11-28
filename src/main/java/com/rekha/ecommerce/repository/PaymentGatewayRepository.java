package com.rekha.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.PaymentGateway;

@Repository
public interface PaymentGatewayRepository
		extends JpaRepository<PaymentGateway, Long>, JpaSpecificationExecutor<PaymentGateway> {

	PaymentGateway findAllByOrderId(String orderId);

}
