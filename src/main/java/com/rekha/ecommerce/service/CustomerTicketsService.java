package com.rekha.ecommerce.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.CustomerTicketImagesDTO;
import com.rekha.ecommerce.dto.CustomerTicketsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.CustomerTicketImages;
import com.rekha.ecommerce.entity.CustomerTickets;
import com.rekha.ecommerce.entity.SecUser;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.CustomerTicketImagesRepository;
import com.rekha.ecommerce.repository.CustomerTicketsRepository;
import com.rekha.ecommerce.repository.SecUserRepository;
import com.rekha.ecommerce.request.CustomerTicketsRequest;
import com.rekha.ecommerce.utils.ImageConversion;

import jakarta.transaction.Transactional;

@Service
public class CustomerTicketsService {

	@Autowired
	private CustomerTicketsRepository ticketsRepository;

	@Autowired
	private CustomerTicketImagesRepository imagesRepository;

	@Autowired
	private SecUserRepository secUserRepository;

	@Transactional
	public ResponseObject<CustomerTicketsRequest> saveTicket(CustomerTicketsRequest customerTicketsRequest,
			String userName, String phoneNumber) {
		try {

			CustomerTickets tickets = new CustomerTickets();
			BeanUtils.copyProperties(customerTicketsRequest.getCustomerTicketsDTO(), tickets);
			tickets.setCustomerName(userName);
			tickets.setTicketId("");
			tickets.setStatus("NEW");
			tickets.setPhoneNumber(phoneNumber);
			tickets.setCreatedBy(userName);

			/*
			 * try { Blob cropImageBlob = ImageConversion
			 * .byteToBlobConversion(customerTicketsRequest.getCustomerTicketsDTO().
			 * getCropImage()); tickets.setCropImage(cropImageBlob); } catch (SQLException
			 * e) { e.printStackTrace(); }
			 */
			Long id = ticketsRepository.save(tickets).getId();
			tickets.setTicketId("TKT#" + id);

			List<CustomerTicketImages> imagesEntList = new ArrayList<>();

			customerTicketsRequest.getTicketImagesList().forEach(image -> {
				CustomerTicketImages imagesEntity = new CustomerTicketImages();
				BeanUtils.copyProperties(image, imagesEntity);
				imagesEntity.setTicketId(id);
				imagesEntity.setCreatedBy(userName);

				Blob probImageBlob = null;
				try {
					probImageBlob = ImageConversion.byteToBlobConversion(image.getImage());
					imagesEntity.setImage(probImageBlob);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				imagesEntList.add(imagesEntity);
			});
			imagesRepository.saveAll(imagesEntList);

			CustomerTicketsDTO dto = new CustomerTicketsDTO();
			BeanUtils.copyProperties(tickets, dto);
			/*
			 * try { byte[] img =
			 * ImageConversion.blobToByteConversion(tickets.getCropImage());
			 * dto.setCropImage(img); } catch (SQLException e) { e.printStackTrace(); }
			 */

			List<CustomerTicketImagesDTO> imagesDTOList = new ArrayList<>();

			imagesEntList.forEach(imgEnt -> {
				CustomerTicketImagesDTO imgDTO = new CustomerTicketImagesDTO();

				BeanUtils.copyProperties(imgEnt, imgDTO);
				byte[] img;
				try {
					img = ImageConversion.blobToByteConversion(imgEnt.getImage());
					imgDTO.setImage(img);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				imagesDTOList.add(imgDTO);
			});

			CustomerTicketsRequest response = new CustomerTicketsRequest();
			response.setCustomerTicketsDTO(dto);
			response.setTicketImagesList(imagesDTOList);
			return ResponseObject.success(response);
		} catch (Exception e2) {
			e2.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);

		}
	}

	@Transactional
	public ResponseObject<CustomerTicketsRequest> updateTicket(CustomerTicketsRequest customerTicketsRequest,
			String userName) {
		try {

			ticketsRepository.findById(customerTicketsRequest.getCustomerTicketsDTO().getId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.NOT_FOUND));
			CustomerTickets tickets = new CustomerTickets();
			BeanUtils.copyProperties(customerTicketsRequest.getCustomerTicketsDTO(), tickets);
			tickets.setLastModifiedBy(userName);
			/*
			 * try { Blob cropImageBlob = ImageConversion
			 * .byteToBlobConversion(customerTicketsRequest.getCustomerTicketsDTO().
			 * getCropImage()); tickets.setCropImage(cropImageBlob); } catch (SQLException
			 * e) { e.printStackTrace(); }
			 */
			ticketsRepository.save(tickets);

			CustomerTicketsDTO dto = new CustomerTicketsDTO();
			BeanUtils.copyProperties(tickets, dto);

			CustomerTicketsRequest response = new CustomerTicketsRequest();
			response.setCustomerTicketsDTO(dto);
			response.setTicketImagesList(customerTicketsRequest.getTicketImagesList());
			return ResponseObject.success(response);

		} catch (CloudBaseException exp) {
			throw exp;
		} catch (Exception e2) {
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);

		}
	}

//	public ResponseObject<List<CustomerTicketsRequest>> getAllTickets() {
//
//		List<CustomerTickets> ticketsList = ticketsRepository.findByStatus("NEW");
//
//		return getListOfTickets(ticketsList);
//	}

	private CustomerTicketsDTO convertToCustomerTicketsDTO(CustomerTickets ticket) {
		CustomerTicketsDTO dto = new CustomerTicketsDTO();
		BeanUtils.copyProperties(ticket, dto);
		/*
		 * try { if (ticket.getCropImage() != null) {
		 * dto.setCropImage(ImageConversion.blobToByteConversion(ticket.getCropImage()))
		 * ; } } catch (SQLException e) { e.printStackTrace(); }
		 */
		return dto;
	}

	private CustomerTicketImagesDTO convertToCustomerTicketImagesDTO(CustomerTicketImages image) {
		CustomerTicketImagesDTO imageDTO = new CustomerTicketImagesDTO();
		BeanUtils.copyProperties(image, imageDTO);

		try {
			if (image.getImage() != null) {
				imageDTO.setImage(ImageConversion.blobToByteConversion(image.getImage()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return imageDTO;
	}

	public ResponseObject<List<CustomerTicketsRequest>> getTickets(String phoneNumber) {
		
		
		List<CustomerTickets> ticketsList = new ArrayList<>();
		try {

			SecUser secUser = secUserRepository.findByPhoneNumberAndIsActive(phoneNumber, true);
			if (secUser != null) {
				if (secUser.getIsAdmin()) {
					ticketsList = ticketsRepository.findAll();
				} else if (secUser.getIsFieldOfficer()) {
					ticketsList = ticketsRepository.findByFieldOfficerPhoneNumber(phoneNumber);
				} else {
					ticketsList = ticketsRepository.findByPhoneNumber(phoneNumber);
				}
				return getListOfTickets(ticketsList);
			} else {
				throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
			}

			
		} catch (CloudBaseException exp) {
			throw exp;
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.UNKNOWN_ERROR_OCCURRED);
		}

	}

	public ResponseObject<List<CustomerTicketsRequest>> getListOfTickets(List<CustomerTickets> ticketsList) {

		List<Long> ticketIds = ticketsList.stream().map(CustomerTickets::getId).collect(Collectors.toList());

		List<CustomerTicketImages> imagesList = imagesRepository.getImagesByTicketIds(ticketIds);

		Map<Long, List<CustomerTicketImages>> ticketImagesMap = imagesList.stream()
				.collect(Collectors.groupingBy(CustomerTicketImages::getTicketId));

		List<CustomerTicketsRequest> responseList = ticketsList.stream().map(ticket -> {

			List<CustomerTicketImages> associatedImages = ticketImagesMap.getOrDefault(ticket.getId(),
					Collections.emptyList());

			List<CustomerTicketImagesDTO> associatedImagesDTO = associatedImages.stream()
					.map(this::convertToCustomerTicketImagesDTO) // Convert each image to DTO
					.collect(Collectors.toList());

			CustomerTicketsDTO ticketDTO = convertToCustomerTicketsDTO(ticket);

			// Create a new response object and set the ticket and its images
			CustomerTicketsRequest response = new CustomerTicketsRequest();
			response.setCustomerTicketsDTO(ticketDTO);
			response.setTicketImagesList(associatedImagesDTO);

			return response;
		}).collect(Collectors.toList());

		return ResponseObject.success(responseList);
	}

}
