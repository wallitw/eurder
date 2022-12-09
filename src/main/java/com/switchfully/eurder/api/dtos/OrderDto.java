package com.switchfully.eurder.api.dtos;

import java.util.List;

public record OrderDto (String orderId, double totalPrice, List<ItemGroupDto> itemGroupDtoList, CustomerDto customer) {
}
