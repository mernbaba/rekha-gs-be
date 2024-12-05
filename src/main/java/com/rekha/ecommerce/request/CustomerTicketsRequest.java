package com.rekha.ecommerce.request;

import java.util.List;

import com.rekha.ecommerce.dto.CustomerTicketImagesDTO;
import com.rekha.ecommerce.dto.CustomerTicketsDTO;

import lombok.Data;

@Data
public class CustomerTicketsRequest {

	CustomerTicketsDTO customerTicketsDTO;

	List<CustomerTicketImagesDTO> ticketImagesList;

}
