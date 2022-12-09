package com.switchfully.eurder.services.mappers;

import com.switchfully.eurder.api.dtos.ItemDto;
import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.api.dtos.StockItemDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {
    public ItemDto toDTO(Item item) {
        return new ItemDto(item.getItemId(), item.getName(), item.getDescription(), item.getPrice(), item.getAmountInStock());
    }

    public List<ItemDto> toDTO(List<Item> allItems) {
        return allItems.stream()
                .map(this::toDTO)
                .toList();
    }

    public StockItemDto toStockItemDto(Item item) {
        return new StockItemDto(item.getItemId(), item.getName(), item.getDescription(), item.getPrice(), item.getAmountInStock(), item.getStockLevel());
    }

    public List<StockItemDto> toStockItemDto(List<Item> allItems) {
        return allItems.stream()
                .map(this::toStockItemDto)
                .toList();
    }




}
