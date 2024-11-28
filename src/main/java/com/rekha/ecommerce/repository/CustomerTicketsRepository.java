package com.rekha.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rekha.ecommerce.entity.CustomerTickets;

public interface CustomerTicketsRepository
		extends JpaRepository<CustomerTickets, Long>, JpaSpecificationExecutor<CustomerTickets> {

}
