package com.rekha.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.rekha.ecommerce.entity.CustomerTicketImages;

public interface CustomerTicketImagesRepository
		extends JpaRepository<CustomerTicketImages, Long>, JpaSpecificationExecutor<CustomerTicketImages> {

	@Query(value = "select * from tb_customer_ticket_images where ticket_id in :ticketIds", nativeQuery = true)
	List<CustomerTicketImages> getImagesByTicketIds(List<Long> ticketIds);

}
