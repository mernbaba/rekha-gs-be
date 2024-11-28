package com.rekha.ecommerce.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.OrderDTO;
import com.rekha.ecommerce.dto.OrderItemsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.Order;
import com.rekha.ecommerce.entity.OrderItems;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.OrderItemsRepository;
import com.rekha.ecommerce.repository.OrderRepository;
import com.rekha.ecommerce.request.OrdersRequest;
import com.rekha.ecommerce.response.OrdersResponse;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	private OrderItemsRepository orderItemsRepository;

	public ResponseObject<?> getAll() {

		List<OrdersResponse> responses = new ArrayList<>();

		List<Order> orderEntities = orderRepository.findAll();

		List<Long> orderIds = orderEntities.stream().map(Order::getId).toList();

		List<OrderItems> orderItemsList = orderItemsRepository.findByOrderId(orderIds);

		orderEntities.forEach(entity -> {
			OrdersResponse response = new OrdersResponse();

			OrderDTO dto = new OrderDTO();
			BeanUtils.copyProperties(entity, dto);
			response.setOrderDTO(dto);

			List<OrderItemsDTO> itemsDTOList = orderItemsList.stream()
					.filter(items -> items.getOrderId().equals(entity.getId())).map(items -> {
						OrderItemsDTO itemsDTO = new OrderItemsDTO();
						BeanUtils.copyProperties(items, itemsDTO);
						if (items.getItemImage() != null) {
							try {
								byte[] brandImageBytes = items.getItemImage().getBytes(1,
										(int) items.getItemImage().length());
								itemsDTO.setItemImage(brandImageBytes);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}

						return itemsDTO;
					}).collect(Collectors.toList());
			response.setOrderItemsDTOList(itemsDTOList);
			responses.add(response);
		});

		return ResponseObject.success(responses);

	}

	public ResponseObject<?> getOrdersByStatus(String status) {

		List<OrderDTO> responseList = new ArrayList<OrderDTO>();

		orderRepository.findByOrderStatus(status).forEach(entity -> {
			OrderDTO dto = new OrderDTO();
			BeanUtils.copyProperties(entity, dto);
			responseList.add(dto);
		});

		return ResponseObject.success(responseList);
	}

	public ResponseObject<?> getUserOrders(String phoneNumber) {
		List<OrdersResponse> responses = new ArrayList<>();

		/*
		 * List<OrderDTO> orderDTOS = new ArrayList<OrderDTO>();
		 * 
		 * orderRepository.findByPhoneNumber(phoneNumber).forEach(entity -> { OrderDTO
		 * dto = new OrderDTO(); BeanUtils.copyProperties(entity, dto);
		 * orderDTOS.add(dto); });
		 */

		List<Order> orderEntities = orderRepository.findByPhoneNumber(phoneNumber);

		List<Long> orderIds = orderEntities.stream().map(Order::getId).toList();

		List<OrderItems> orderItemsList = orderItemsRepository.findByOrderId(orderIds);

		orderEntities.forEach(entity -> {
			OrdersResponse response = new OrdersResponse();

			OrderDTO dto = new OrderDTO();
			BeanUtils.copyProperties(entity, dto);
			response.setOrderDTO(dto);

			List<OrderItemsDTO> itemsDTOList = orderItemsList.stream()
					.filter(items -> items.getOrderId().equals(entity.getId())).map(items -> {
						OrderItemsDTO itemsDTO = new OrderItemsDTO();
						BeanUtils.copyProperties(items, itemsDTO);
						if (items.getItemImage() != null) {
							try {
								byte[] brandImageBytes = items.getItemImage().getBytes(1,
										(int) items.getItemImage().length());
								itemsDTO.setItemImage(brandImageBytes);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}

						return itemsDTO;
					}).collect(Collectors.toList());
			response.setOrderItemsDTOList(itemsDTOList);
			responses.add(response);
		});

		return ResponseObject.success(responses);
	}

	@Transactional
	public ResponseObject<?> save(OrdersRequest dto, String phoneNumber, String username) {

		try {
			Order orderEntity = new Order();
			BeanUtils.copyProperties(dto.getOrderDTO(), orderEntity);

			orderEntity.setPhoneNumber(phoneNumber);
			orderEntity.setOrderId("");
			orderEntity.setCustomerName(username);
			orderEntity.setCreatedBy(username);

			Long id = orderRepository.save(orderEntity).getId();
			orderEntity.setOrderId("ORD#" + id);
			orderRepository.save(orderEntity);

			List<OrderItems> orderItemsList = new ArrayList<>();
			dto.getOrderItemsDTOList().forEach(items -> {
				OrderItems orderItems = new OrderItems();
				BeanUtils.copyProperties(items, orderItems);
				byte[] itemImageBytes = items.getItemImage();
				try {
					Blob itemImageBlob = new SerialBlob(itemImageBytes);
					orderItems.setItemImage(itemImageBlob);
				} catch (Exception e) {
					e.printStackTrace();
				}

				orderItems.setCreatedBy(username);
				orderItems.setOrderId(id);
				orderItemsList.add(orderItems);

			});
			orderItemsRepository.saveAll(orderItemsList);

			OrderDTO orderDTO = new OrderDTO();
			BeanUtils.copyProperties(orderEntity, orderDTO);

			List<OrderItemsDTO> itemsDTOList = new ArrayList<>();
			orderItemsList.forEach(itemsEntity -> {
				OrderItemsDTO itemsDTO = new OrderItemsDTO();
				BeanUtils.copyProperties(itemsEntity, itemsDTO);
				if (itemsEntity.getItemImage() != null) {
					try {
						byte[] brandImageBytes = itemsEntity.getItemImage().getBytes(1,
								(int) itemsEntity.getItemImage().length());
						itemsDTO.setItemImage(brandImageBytes);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				itemsDTOList.add(itemsDTO);
			});
			OrdersResponse response = new OrdersResponse();

			response.setOrderDTO(orderDTO);
			response.setOrderItemsDTOList(itemsDTOList);

			return ResponseObject.success(response);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	@Transactional
	public ResponseObject<?> update(OrdersRequest orderRequest, String phoneNumber, String username) {
		try {
			Order orderEntity = orderRepository.findById(orderRequest.getOrderDTO().getId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.DATA_NOT_FOUND));
			BeanUtils.copyProperties(orderRequest.getOrderDTO(), orderEntity);

			orderEntity.setLastModifiedBy(username);

			orderRepository.save(orderEntity);

			OrderDTO response = new OrderDTO();
			BeanUtils.copyProperties(orderEntity, response);

			return ResponseObject.success(response);
		} catch (CloudBaseException exp) {
			exp.printStackTrace();
			throw exp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	/*
	 * private static String generateOrderID() { SimpleDateFormat dateFormat = new
	 * SimpleDateFormat("yyyyMMddHHmmss"); String timestamp = dateFormat.format(new
	 * Date());
	 * 
	 * Random random = new Random(); int randomNum = random.nextInt(9000) + 1000;
	 * 
	 * return "ORD#" + timestamp + randomNum; }
	 */

}
