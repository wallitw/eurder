package com.switchfully.eurder.domain.repositories;

import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    public Order createOrder(Order order) {
        orders.add(order);
        return order;
    }

    public List<Order> getCustomerOrders(CustomerDto customer) {
        return orders.stream()
                .filter(order -> order.getCustomer().equals(customer)).toList();
    }
}
