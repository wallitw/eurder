package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dtos.CreateOrderDto;
import com.switchfully.eurder.api.dtos.OrderDto;
import com.switchfully.eurder.api.dtos.OrderReportDto;
import com.switchfully.eurder.domain.security.Feature;
import com.switchfully.eurder.services.OrderService;
import com.switchfully.eurder.services.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "orders")
public class OrderController {
    private final OrderService orderService;
    private final SecurityService securityService;

    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody CreateOrderDto createOrderDto, @RequestHeader String authorization) {
        securityService.validateAuthorisation(authorization, Feature.CREATE_ORDER);
        return orderService.createOrder(createOrderDto, authorization);
    }

    @GetMapping(path = "/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderReportDto getCustomerOrders(@RequestHeader String authorization, @PathVariable String userName) {
        securityService.validateAuthorisation(authorization, Feature.GET_CUSTOMER_ORDERS);
        return orderService.getCustomerOrders(authorization, userName);
    }


}
