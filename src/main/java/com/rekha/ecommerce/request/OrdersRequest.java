package com.rekha.ecommerce.request;

import java.util.List;

import com.rekha.ecommerce.dto.OrderDTO;
import com.rekha.ecommerce.dto.OrderItemsDTO;

import lombok.Data;

@Data
public class OrdersRequest {

    OrderDTO orderDTO;

    List<OrderItemsDTO> orderItemsDTOList;
}
