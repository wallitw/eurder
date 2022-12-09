package com.switchfully.eurder.services;


import com.switchfully.eurder.api.dtos.CreateItemDto;
import com.switchfully.eurder.api.dtos.ItemDto;
import com.switchfully.eurder.api.dtos.StockItemDto;
import com.switchfully.eurder.api.dtos.UpdateItemDto;
import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.domain.exceptions.ItemAlreadyExistsException;
import com.switchfully.eurder.domain.exceptions.ItemDoesNotExistException;
import com.switchfully.eurder.domain.repositories.ItemRepository;
import com.switchfully.eurder.services.mappers.ItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        //input validation
        String error = validateInput(createItemDto);
        if (!error.isEmpty()) throw new IllegalArgumentException("Following fields are invalid:" + error);
        //check if item already exists
        if (checkIfItemAlreadyExists(createItemDto)) {
            throw new ItemAlreadyExistsException("The item you tried to create with name: " + createItemDto.name() + " already exists, please use updateItem with following id: " + itemRepository.getItemByName(createItemDto.name()).orElseThrow().getItemId() + " to change price or amount in stock");
        }
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

    private boolean checkIfItemAlreadyExists(CreateItemDto createItemDto) {
        return itemRepository.getItemByName(createItemDto.name()).isPresent();
    }

    public ItemDto updateItem(UpdateItemDto updateItemDto, String id) {
        Item item = itemRepository.getItemById(id).orElseThrow(() -> new ItemDoesNotExistException("The item you want to update does not exist"));
        if (!updateItemDto.name().isEmpty()) {
            item.setName(updateItemDto.name());
        }
        if (!updateItemDto.description().isEmpty()) {
            item.setDescription(updateItemDto.description());
        }
        if (updateItemDto.price() > 0) {
            item.setPrice(updateItemDto.price());
        }
        if (updateItemDto.amountInStock() >= 0) {
            item.setAmountInStock(updateItemDto.amountInStock());
        }
        return itemMapper.toDTO(item);
    }

    public List<ItemDto> getAllItems() {
        return itemMapper.toDTO(itemRepository.getAllItems());
    }

    public List<StockItemDto> getAllItemsStockLevel() {
        for (Item item : itemRepository.getAllItems()) {
            item.setStockLevel();
        }
        return itemMapper.toStockItemDto(itemRepository.getAllItems().stream().sorted(Comparator.comparing((Item::getAmountInStock))).collect(Collectors.toList()));
    }

    public List<StockItemDto> getItemsOfStockLevel(String stocklevel) {
        return getAllItemsStockLevel().stream().filter(stockItemDto -> stockItemDto.stockLevel().toString().equalsIgnoreCase(stocklevel)).collect(Collectors.toList());
    }
}
