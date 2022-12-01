package com.switchfully.eurder.domain.repositories;

import com.switchfully.eurder.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class OrderRepository {
    private final List<Order> orderMap = new ArrayList<>();

    public Order createOrder(Order order) {
        orderMap.add(order);
        return order;
    }
}
