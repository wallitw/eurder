package com.switchfully.eurder.api;


import com.switchfully.eurder.api.dtos.CreateOrderDto;
import com.switchfully.eurder.api.dtos.OrderDto;
import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.domain.ItemGroup;
import com.switchfully.eurder.domain.repositories.ItemRepository;
import com.switchfully.eurder.domain.repositories.UserRepository;
import com.switchfully.eurder.domain.security.Role;
import com.switchfully.eurder.services.mappers.UserMapper;
import io.restassured.RestAssured;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @LocalServerPort
    int port;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserMapper userMapper;


    @Test
    void orderItem_whenCustomerAndItemInStock_happyPath() {
        Customer wally = new Customer("wally", "pwd", Role.CUSTOMER, "William", "Multani", "william.multani@gmail.com", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"), "0485300304");
        userRepository.createCustomer(wally);
        Item testItem = new Item("Laptop HP10", "i5 Processor", 1000, 2);
        itemRepository.createItem(testItem);
        List<ItemGroup> itemsToOrder = new ArrayList<>();
        itemsToOrder.add(new ItemGroup(testItem.getItemId(), 2));
        CreateOrderDto createOrderDto = new CreateOrderDto(itemsToOrder);
        OrderDto result =
                RestAssured
                        .given().port(port).auth().preemptive().basic("wally", "pwd").log().all().contentType("application/json").body(createOrderDto)
                        .when().post("/orders")
                        .then().statusCode(201).and().extract().as(OrderDto.class);

        assertEquals(userMapper.toDTO(wally), result.customer());
        assertEquals(LocalDate.now().plusDays(1), result.itemGroupList().get(0).getShippingDate());
        assertEquals(testItem.getItemId(), result.itemGroupList().get(0).getItemId());
    }

    @Test
    void orderItem_whenCustomerAndItemNotInStock_happyPathShippingDatePlus7() {
        Customer wally = new Customer("wally", "pwd", Role.CUSTOMER, "William", "Multani", "william.multani@gmail.com", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"), "0485300304");
        userRepository.createCustomer(wally);
        Item testItem = new Item("Laptop HP10", "i5 Processor", 1000, 2);
        itemRepository.createItem(testItem);
        List<ItemGroup> itemsToOrder = new ArrayList<>();
        itemsToOrder.add(new ItemGroup(testItem.getItemId(), 15));
        CreateOrderDto createOrderDto = new CreateOrderDto(itemsToOrder);
        OrderDto result =
                RestAssured
                        .given().port(port).auth().preemptive().basic("wally", "pwd").log().all().contentType("application/json").body(createOrderDto)
                        .when().post("/orders")
                        .then().statusCode(201).and().extract().as(OrderDto.class);

        assertEquals(userMapper.toDTO(wally), result.customer());
        assertEquals(LocalDate.now().plusDays(7), result.itemGroupList().get(0).getShippingDate());
        assertEquals(testItem.getItemId(), result.itemGroupList().get(0).getItemId());
    }

    @Test
    void orderItem_whenCustomerWrongPassword_Forbidden() {
        Customer wally = new Customer("wally", "pwd", Role.CUSTOMER, "William", "Multani", "william.multani@gmail.com", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"), "0485300304");
        userRepository.createCustomer(wally);
        Item testItem = new Item("Laptop HP10", "i5 Processor", 1000, 2);
        itemRepository.createItem(testItem);
        List<ItemGroup> itemsToOrder = new ArrayList<>();
        itemsToOrder.add(new ItemGroup(testItem.getItemId(), 15));
        CreateOrderDto createOrderDto = new CreateOrderDto(itemsToOrder);
        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("wally", "pwd12").log().all().contentType("application/json").body(createOrderDto)
                        .when().post("/orders")
                        .then().statusCode(403).and().extract().as(JSONObject.class);
        assertEquals("Unauthorized", response.get("message").toString());
    }

    @Test
    void orderItem_whenNoAuthorization_Forbidden() {
        Customer wally = new Customer("wally", "pwd", Role.CUSTOMER, "William", "Multani", "william.multani@gmail.com", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"), "0485300304");
        userRepository.createCustomer(wally);
        Item testItem = new Item("Laptop HP10", "i5 Processor", 1000, 2);
        itemRepository.createItem(testItem);
        List<ItemGroup> itemsToOrder = new ArrayList<>();
        itemsToOrder.add(new ItemGroup(testItem.getItemId(), 15));
        CreateOrderDto createOrderDto = new CreateOrderDto(itemsToOrder);
                RestAssured
                        .given().port(port).contentType("application/json").body(createOrderDto)
                        .when().post("/orders")
                        .then().statusCode(400);
    }

    @Test
    void orderItem_whenCustomerAndItemDoesNotExist_thenBadRequestAndCustomMessage() {
        Customer wally = new Customer("wally", "pwd", Role.CUSTOMER, "William", "Multani", "william.multani@gmail.com", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"), "0485300304");
        userRepository.createCustomer(wally);
        List<ItemGroup> itemsToOrder = new ArrayList<>();
        itemsToOrder.add(new ItemGroup("123", 15));
        CreateOrderDto createOrderDto = new CreateOrderDto(itemsToOrder);
        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("wally", "pwd").log().all().contentType("application/json").body(createOrderDto)
                        .when().post("/orders")
                        .then().statusCode(400).and().extract().as(JSONObject.class);
        assertEquals("The item you tried to order does not exist. The order is cancelled.", response.get("message").toString());

    }
}
