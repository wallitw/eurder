package com.switchfully.eurder.domain;

import java.util.UUID;

public class Item {

    private final String itemId;
    private String name;
    private String description;
    private double price;
    private int amountInStock;

    private StockLevel stockLevel;

    public Item(String name, String description, double price, int amountInStock) {
        this.itemId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.amountInStock = amountInStock;
    }

    public String getItemId() {
        return itemId;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    public void setStockLevel() {
        if (this.amountInStock < 5) {
            this.stockLevel = StockLevel.STOCK_LOW;
        }
        else if (this.amountInStock < 10) {
            this.stockLevel = StockLevel.STOCK_MEDIUM;
        }
        else {
            this.stockLevel = StockLevel.STOCK_HIGH;
        }
    }

    public StockLevel getStockLevel() {
        return stockLevel;
    }
}
