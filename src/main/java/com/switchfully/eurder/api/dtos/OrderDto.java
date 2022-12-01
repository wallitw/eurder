package com.switchfully.eurder.api.dtos;

import com.switchfully.eurder.domain.ItemGroup;

import java.util.List;

public record OrderDto (String orderId, double totalPrice, List<ItemGroup> itemGroupList, CustomerDto customer) {
}
