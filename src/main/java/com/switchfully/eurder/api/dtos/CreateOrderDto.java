package com.switchfully.eurder.api.dtos;

import com.switchfully.eurder.domain.ItemGroup;

import java.util.List;

public record CreateOrderDto(List<ItemGroup> itemGroupList) {
}
