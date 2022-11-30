package com.switchfully.eurder.api.dtos;

public record UpdateItemDto(String name, String description, double price, int amountInStock) {
}
