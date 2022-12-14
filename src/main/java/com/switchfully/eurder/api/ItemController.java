package com.switchfully.eurder.api;


import com.switchfully.eurder.api.dtos.CreateItemDto;
import com.switchfully.eurder.api.dtos.ItemDto;
import com.switchfully.eurder.api.dtos.StockItemDto;
import com.switchfully.eurder.api.dtos.UpdateItemDto;
import com.switchfully.eurder.domain.security.Feature;
import com.switchfully.eurder.services.ItemService;
import com.switchfully.eurder.services.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "items")
public class ItemController {
    private final ItemService itemService;
    private final SecurityService securityService;

    public ItemController(ItemService itemService, SecurityService securityService) {
        this.itemService = itemService;
        this.securityService = securityService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody CreateItemDto createItemDto, @RequestHeader String authorization) {
        securityService.validateAuthorisation(authorization, Feature.CREATE_ITEM);
        return itemService.createItem(createItemDto);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemDto updateItem(@RequestBody UpdateItemDto updateItemDto, @RequestHeader String authorization, @PathVariable String id) {
        securityService.validateAuthorisation(authorization, Feature.UPDATE_ITEM);
        return itemService.updateItem(updateItemDto, id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ItemDto> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping(path = "/stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StockItemDto> getAllItemsStockLevel(@RequestHeader String authorization) {
        securityService.validateAuthorisation(authorization, Feature.GET_STOCK_LEVEL);
        return itemService.getAllItemsStockLevel();
    }

    @GetMapping(path = "/stock", params = "stocklevel" , produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StockItemDto> getStockLevel(@RequestHeader String authorization, @RequestParam String stocklevel) {
        securityService.validateAuthorisation(authorization, Feature.GET_STOCK_LEVEL);
        return itemService.getItemsOfStockLevel(stocklevel);

    }
}
