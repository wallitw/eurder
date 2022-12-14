package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dtos.*;
import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.Item;
import com.switchfully.eurder.domain.StockLevel;
import com.switchfully.eurder.domain.repositories.ItemRepository;
import com.switchfully.eurder.domain.repositories.UserRepository;
import com.switchfully.eurder.domain.security.Role;
import com.switchfully.eurder.services.mappers.ItemMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemControllerTest {
    @LocalServerPort
    int port;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemMapper itemMapper;

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
    void createItem_whenAdminAndEmptyFields_thenMessageContainsInvalidFields() {
        CreateItemDto createItemDto = new CreateItemDto("", "", -7, -1);

        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("admin", "pwd").log().all().contentType(ContentType.JSON).body(createItemDto)
                        .when().post("/items")
                        .then().statusCode(400).and().extract().as(JSONObject.class);
        assertEquals("Following fields are invalid: name description price amountInStock", response.get("message").toString());
    }

    @Test
    void createItem_whenAdminAndWrongPassword_thenForbiddenAndMessageUnauthorized() {
        CreateItemDto createItemDto = new CreateItemDto("Laptop", "HP 10", 875.0, 1);

        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("admin", "pwd123").log().all().contentType(ContentType.JSON).body(createItemDto)
                        .when().post("/items")
                        .then().statusCode(403).and().extract().as(JSONObject.class);
        assertEquals("Unauthorized", response.get("message").toString());
    }

    @Test
    void createItem_whenCustomer_thenForbiddenAndMessageUnauthorized() {
        userRepository.createCustomer(new Customer("wally", "pwd", Role.CUSTOMER, "William", "Multani", "william.multani@gmail.com", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"), "0485300304"));
        CreateItemDto createItemDto = new CreateItemDto("Laptop", "HP 10", 875.0, 1);

        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("wally", "pwd").log().all().contentType(ContentType.JSON).body(createItemDto)
                        .when().post("/items")
                        .then().statusCode(403).and().extract().as(JSONObject.class);
        assertEquals("Unauthorized", response.get("message").toString());
    }

    @Test
    void createItem_whenUserNotFound_thenForbiddenAndMessageUnauthorized() {
        CreateItemDto createItemDto = new CreateItemDto("Laptop", "HP 10", 875.0, 1);

        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("testerbestaatniet", "pwd").log().all().contentType(ContentType.JSON).body(createItemDto)
                        .when().post("/items")
                        .then().statusCode(403).and().extract().as(JSONObject.class);
        assertEquals("Unauthorized", response.get("message").toString());
    }

    @Test
    void createItem_whenItemAlreadyExists_thenBadRequestAndMessageContainsItemName() {
        Item testItem = new Item("Laptop HP10", "maakt niet uit wat ik hier zet", 900, 6);
        itemRepository.createItem(testItem);
        CreateItemDto createItemDto = new CreateItemDto("Laptop HP10", "HP 10 15inch i5 core", 1100, 7);

        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("admin", "pwd").log().all().contentType(ContentType.JSON).body(createItemDto)
                        .when().post("/items")
                        .then().statusCode(400).and().extract().as(JSONObject.class);
        assertEquals("The item you tried to create with name: Laptop HP10 already exists, please use updateItem with following id: " + testItem.getItemId() + " to change price or amount in stock", response.get("message").toString());
    }

    @Test
    void updateItemWithAllFields_whenAdmin_happyPath() {
        Item testItem = new Item("Laptop HP10", "slechte omschrijving ", 100, 6);
        itemRepository.createItem(testItem);
        UpdateItemDto updateItemDto = new UpdateItemDto("Updated Laptop HP10", "maakt niet uit wat ik hier zet", 900, 5);
        ItemDto result =
                RestAssured
                        .given().port(port).auth().preemptive().basic("admin", "pwd").log().all().contentType("application/json").body(updateItemDto)
                        .when().put("/items/" + testItem.getItemId())
                        .then().statusCode(200).and().extract().as(ItemDto.class);
        assertEquals("Updated Laptop HP10", result.name());
        assertEquals("maakt niet uit wat ik hier zet", result.description());
        assertEquals(900, result.price());
        assertEquals(5, result.amountInStock());
    }

    @Test
    void updateItemWithAllFields_whenAdminAndItemDoesNotExist_thenException() {
        Item testItem = new Item("Laptop HP10", "slechte omschrijving ", 100, 6);
        itemRepository.createItem(testItem);
        UpdateItemDto updateItemDto = new UpdateItemDto("Updated Laptop HP10", "maakt niet uit wat ik hier zet", 900, 5);
        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("admin", "pwd").log().all().contentType("application/json").body(updateItemDto)
                        .when().put("/items/123")
                        .then().statusCode(400).and().extract().as(JSONObject.class);
        assertEquals("The item you want to update does not exist", response.get("message").toString());
    }

    @Test
    void viewItemOverviewNoParams_whenAdminThenStockItemsHaveStockLevelAndAreOrdered_happyPath() {
        Item testItem = new Item("Laptop HP10", "slechte omschrijving ", 100, 6);
        Item testItem2 = new Item("Mouse", "slechte omschrijving ", 100, 11);
        Item testItem3 = new Item("Screen", "slechte omschrijving ", 10, 0);

        itemRepository.createItem(testItem);
        itemRepository.createItem(testItem2);
        itemRepository.createItem(testItem3);
        List<StockItemDto> stockitems =
                RestAssured.given().port(port).contentType("application/json").auth().preemptive().basic("admin", "pwd")
                        .when().get("/items/stock")
                        .then().statusCode(200).and().extract().body().jsonPath().getList(".", StockItemDto.class);

        assertEquals(StockLevel.STOCK_HIGH, stockitems.get(2).stockLevel());
        assertEquals(StockLevel.STOCK_MEDIUM, stockitems.get(1).stockLevel());
        assertEquals(StockLevel.STOCK_LOW, stockitems.get(0).stockLevel());
    }

    @Test
    void viewItemOverviewWithParams_whenAdminThenStockItemsHaveStockLevelAndAreOrdered_happyPath() {
        Item testItem = new Item("Laptop HP10", "slechte omschrijving ", 100, 6);
        Item testItem2 = new Item("Mouse", "slechte omschrijving ", 100, 11);
        Item testItem3 = new Item("Screen", "slechte omschrijving ", 10, 0);

        itemRepository.createItem(testItem);
        itemRepository.createItem(testItem2);
        itemRepository.createItem(testItem3);
        List<StockItemDto> stockitems =
                RestAssured.given().port(port).contentType("application/json").auth().preemptive().basic("admin", "pwd")
                        .when().get("/items/stock?stocklevel=stock_low")
                        .then().statusCode(200).and().extract().body().jsonPath().getList(".", StockItemDto.class);

        assertEquals(StockLevel.STOCK_LOW, stockitems.get(0).stockLevel());
    }


}