package com.switchfully.eurder.domain;

import java.time.LocalDate;

public class ItemGroup {
    private final String itemId;
    private final int amount;
    private LocalDate shippingDate;


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

    @Override
    public String toString() {
        return "ItemGroup{" +
                "itemId='" + itemId + '\'' +
                ", amount=" + amount +
                ", shippingDate=" + shippingDate +
                '}';
    }
}
