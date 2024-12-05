package com.rekha.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rekha.ecommerce.entity.CustomerTickets;

public interface CustomerTicketsRepository
		extends JpaRepository<CustomerTickets, Long>, JpaSpecificationExecutor<CustomerTickets> {

	List<CustomerTickets> findByStatus(String status);

	List<CustomerTickets> findByFieldOfficerPhoneNumber(String phoneNumber);

	List<CustomerTickets> findByPhoneNumber(String phoneNumber);

}
