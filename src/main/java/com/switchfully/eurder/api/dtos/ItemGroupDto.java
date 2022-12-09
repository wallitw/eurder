package com.switchfully.eurder.api.dtos;

import java.time.LocalDate;

public record ItemGroupDto (String itemId, String itemName, int amount, LocalDate shippingDate, double unitPriceAtMoment, double totalItemGroupPrice){
}
