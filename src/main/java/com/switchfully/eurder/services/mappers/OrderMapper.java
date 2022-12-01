package com.switchfully.eurder.services.mappers;

import com.switchfully.eurder.api.dtos.OrderDto;
import com.switchfully.eurder.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderDto toDTO(Order order) {
        return new OrderDto(order.getOrderId(), order.getTotalPrice(), order.getItemGroupList(), order.getCustomer());
    }
}
