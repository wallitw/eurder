package com.switchfully.eurder.domain;

import com.switchfully.eurder.api.dtos.CustomerDto;

import java.util.List;
import java.util.UUID;

public class Order {
    private final String orderId;
    private final double totalPrice;
    private final List<ItemGroup> itemGroupList;
    private final CustomerDto customer;

    public Order(double totalPrice, List<ItemGroup> itemGroupList, CustomerDto customer) {
        this.orderId = UUID.randomUUID().toString();
        this.totalPrice = totalPrice;
        this.itemGroupList = itemGroupList;
        this.customer = customer;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<ItemGroup> getItemGroupList() {
        return itemGroupList;
    }

    public CustomerDto getCustomer() {
        return customer;
    }
}
