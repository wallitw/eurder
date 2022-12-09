package com.switchfully.eurder.api.dtos;

import com.switchfully.eurder.domain.StockLevel;

public record StockItemDto (String itemId, String name, String description, double price, int amountInStock, StockLevel stockLevel) {
}
