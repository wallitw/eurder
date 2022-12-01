package com.switchfully.eurder.services.mappers;

import com.switchfully.eurder.api.dtos.ItemDto;
import com.switchfully.eurder.domain.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    public ItemDto toDTO(Item item) {
        return new ItemDto(item.getItemId(), item.getName(), item.getDescription(), item.getPrice(), item.getAmountInStock());
    }
}
