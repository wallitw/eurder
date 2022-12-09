package com.switchfully.eurder.api.dtos;

import java.util.List;

public record OrderReportDto (double totalPriceOfAllOrders, List<OrderDto> orders){
}
