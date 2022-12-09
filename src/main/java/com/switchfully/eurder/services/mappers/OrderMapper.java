package com.switchfully.eurder.services.mappers;

import com.switchfully.eurder.api.dtos.ItemGroupDto;
import com.switchfully.eurder.api.dtos.OrderDto;
import com.switchfully.eurder.domain.ItemGroup;
import com.switchfully.eurder.domain.Order;
import com.switchfully.eurder.domain.repositories.ItemRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    private final ItemRepository itemRepository;

    public OrderMapper(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public OrderDto toDTO(Order order) {
        return new OrderDto(order.getOrderId(), order.getTotalPrice(), createItemGroupDtoList(order.getItemGroupList()), order.getCustomer());
    }

    public List<ItemGroupDto> createItemGroupDtoList(List<ItemGroup> itemGroupList) {
        return itemGroupList.stream()
                .map(itemGroup -> new ItemGroupDto(itemGroup.getItemId(), itemRepository.getItemById(itemGroup.getItemId()).orElseThrow().getName(), itemGroup.getAmount(), itemGroup.getShippingDate(), itemRepository.getItemById(itemGroup.getItemId()).orElseThrow().getPrice(), itemRepository.getItemById(itemGroup.getItemId()).orElseThrow().getPrice() * itemGroup.getAmount()))
                .toList();
    }


    public List<OrderDto> toDTO(List<Order> allOrders) {
        return allOrders.stream()
                .map(this::toDTO)
                .toList();
    }


}
