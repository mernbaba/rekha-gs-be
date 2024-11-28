package com.rekha.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rekha.ecommerce.entity.CustomerTicketImages;

public interface CustomerTicketImagesRepository
		extends JpaRepository<CustomerTicketImages, Long>, JpaSpecificationExecutor<CustomerTicketImages> {

}
