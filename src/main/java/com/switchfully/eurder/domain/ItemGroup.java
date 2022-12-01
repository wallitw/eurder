package com.switchfully.eurder.domain;

import java.time.LocalDate;

public class ItemGroup {
    private final String itemId;
    private final int amount;
    private LocalDate shippingDate;

    private double priceAtMoment;

    public ItemGroup(String itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
        this.shippingDate = LocalDate.now().plusDays(7);
    }

    public String getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public void setPriceAtMoment(double priceAtMoment) {
        this.priceAtMoment = priceAtMoment;
    }
}
