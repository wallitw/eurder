package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dtos.CreateItemDto;
import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.api.dtos.ItemDto;
import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.repositories.ItemRepository;
import com.switchfully.eurder.domain.repositories.UserRepository;
import com.switchfully.eurder.domain.security.Role;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest {
    @LocalServerPort
    int port;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void createItem_whenAdmin_happyPath() {
        CreateItemDto createItemDto = new CreateItemDto("Laptop", "HP 10", 875.0, 1);

        ItemDto result =
                RestAssured
                        .given().port(port).auth().preemptive().basic("admin", "pwd").log().all().contentType("application/json").body(createItemDto)
                        .when().post("/items")
                        .then().statusCode(201).and().extract().as(ItemDto.class);
        assertEquals("Laptop", result.name());
    }

    @Test
    void createItem_whenCustomer_thenForbiddenAndMessageUnauthorized() {
        userRepository.createCustomer(new Customer("wally", "pwd", Role.CUSTOMER, "William", "Multani", "william.multani@gmail.com", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"), "0485300304"));
        CreateItemDto createItemDto = new CreateItemDto("Laptop", "HP 10", 875.0, 1);

        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("wally", "pwd").log().all().contentType(ContentType.JSON).body(createItemDto).accept(ContentType.JSON)
                        .when().post("/items")
                        .then().statusCode(403).and().extract().as(JSONObject.class);
        assertEquals("Unauthorized", response.get("message").toString());
    }
}