package com.switchfully.eurder.services;


import com.switchfully.eurder.api.dtos.CreateItemDto;
import com.switchfully.eurder.api.dtos.ItemDto;
import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.domain.repositories.ItemRepository;
import com.switchfully.eurder.services.mappers.ItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final Logger logger = LoggerFactory.getLogger(ItemService.class);

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDto createItem(CreateItemDto createItemDto) {
        String error = validateInput(createItemDto);
        if (!error.isEmpty()) throw new IllegalArgumentException("Following fields are invalid: " + error);
        Item item = new Item(createItemDto.name(), createItemDto.description(), createItemDto.price(), createItemDto.amountInStock());
        return itemMapper.toDTO(itemRepository.createItem(item));
    }

    private String validateInput(CreateItemDto createItemDto) {
        String result = "";
        if (createItemDto.name().isEmpty()) {
            result += " name";
        }
        if (createItemDto.description().isEmpty()) {
            result += " description";
        }
        if (createItemDto.price() <= 0) {
            result += " price";
        }
        if (createItemDto.amountInStock() < 0) {
            result += " amountInStock";
        }
        return result;
    }
}
