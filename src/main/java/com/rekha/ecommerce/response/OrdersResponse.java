package com.rekha.ecommerce.response;

import com.rekha.ecommerce.dto.OrderDTO;
import com.rekha.ecommerce.dto.OrderItemsDTO;
import lombok.Data;

import java.util.List;

@Data
public class OrdersResponse {

    OrderDTO orderDTO;

    List<OrderItemsDTO> orderItemsDTOList;
}
